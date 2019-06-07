# api/views.py
from django.shortcuts import render
from rest_framework import generics
from .serializers import *
from rest_framework import permissions
from .permissions import IsOwner
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status
import cv2 as cv
from PIL import Image
import os
import numpy as np



from .models import ClassRoom
from .models import StudentAttendance


class Logout(APIView):
    def get(self, request, format=None):
        request.user.auth_token.delete()
        return Response(status=status.HTTP_200_OK)

class LandingPageView(generics.RetrieveAPIView):
    """View to retrieve a user instance."""
    queryset = User.objects.all()
    serializer_class = LandingPageSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)


class ListClassRoomView(generics.ListCreateAPIView):
    queryset = ClassRoom.objects.all()
    serializer_class = ClassRoomSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)

    def get_queryset(self, *args, **kwargs):

        return ClassRoom.objects.all().filter(instructor=self.request.user)

class SListClassRoomView(APIView):

    permission_classes = (
        permissions.IsAuthenticated,
        )

    def get(self, *args, **kwargs):
        id = self.request.user.id
        x = ClassStudent.objects.filter(student__id=id)
        lst = []
        for each in x:
            mp={}
            mp['id'] = each.classroom.id
            mp['course_no']= each.classroom.course_no
            mp['name']= each.classroom.name
            lst.append(mp)

        return Response(lst)


class CreateClassRoomView(generics.ListCreateAPIView):
    serializer_class = CreateClassRoomSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)

    def get_queryset(self, *args, **kwargs):
        return ClassRoom.objects.all().filter(instructor=self.request.user)

    def perform_create(self, serializer):
        serializer.save(instructor=self.request.user)

class CreateAttendanceView(generics.ListCreateAPIView):
    queryset = Attendance.objects.all()
    serializer_class = AttendanceSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)

    # def get_queryset(self, *args, **kwargs):
    #     return Attendance.objects.all().filter(class_room__instructor=self.request.user)

    def perform_create(self, serializer):
        serializer.save()

class CreateAttendanceImageView(generics.ListCreateAPIView):
    queryset = DJImage.objects.all()
    serializer_class = ImageSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)

    def perform_create(self, serializer):
        serializer.save()

class DetailsClassRoomView(generics.RetrieveUpdateDestroyAPIView):
    queryset = ClassRoom.objects.all()
    serializer_class = ClassRoomSerializer
    permission_classes = (
        permissions.IsAuthenticated,
        IsOwner)

class DetailsAtView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = AttendanceSerializerExtended
    permission_classes = (
        permissions.IsAuthenticated,
    )

    def get(self, request, *args, **kwargs):
        at_id = self.kwargs['at_id']
        serializer = AttendanceSerializerExtended(Attendance.objects.get(id=at_id))
        return Response(serializer.data)

class CreateJoinClassView(generics.ListCreateAPIView):
    queryset = StudentAttendance.objects.all()
    serializer_class = ClassStudentSerializer
    permission_classes = (
        permissions.IsAuthenticated,
    )

    def perform_create(self, serializer):
        serializer.save(student=self.request.user)

    # def get(self, serializer):
    #     return StudentAttendance.objects.filter(student=self.request.user)



class CreateUserView(generics.CreateAPIView):
    permission_classes = [
        permissions.AllowAny
    ]
    serializer_class = UserSerializer

class AllUserView(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer2
    permission_classes = [
        permissions.AllowAny
    ]

#Which days of a class I was present

class DayAttendanceView(APIView):
    # queryset = StudentAttendance.objects.all()
    permission_classes = (
        permissions.IsAuthenticated,)

    def get(self, request, *args, **kwargs):
        class_id = self.kwargs['class_id']

        SA= StudentAttendance.objects.filter(student=self.request.user, attendance__class_room__id = class_id)
        att= Attendance.objects.filter(class_room__id = class_id)

        lst = []
        for each in SA:
           lst.append(each.attendance.id)

        lst_day=[]

        for each in att:
            present = "Not Present"
            if each.id not in lst:
                present = "Present"

            mp={}
            mp['day'] = str(each.date_created)
            mp['status'] = present
            lst_day.append(mp)

        return Response(lst_day)



class CalcAttendanceView(APIView):
    permission_classes = (
        permissions.IsAuthenticated,)



    def draw_rectangle(self, img, rect):
       (x, y, w, h) =rect
       cv.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)

    def draw_text(self, img, text, x, y):
        cv.putText(img, text, (x, y), cv.FONT_HERSHEY_PLAIN, 1.5, (0, 255, 0), 2)


    def detect_face(self, img):
        gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
        print(os.getcwd())
        face_cascade = cv.CascadeClassifier(os.getcwd()+os.path.sep+'haarcascade_frontalface_alt2.xml')
        face_rect= face_cascade.detectMultiScale(gray);
        if (len(face_rect) == 0):
            return None, None
        lst_faces=[]
        for (x, y, w, h) in face_rect:
            lst_faces.append(gray[y:y + w, x:x + h])
        return lst_faces, face_rect

    def doSomething(self, img, student_faces, student_ids):
        lst_id=[]
        lst_grid=[]
        faces,face_rect = self.detect_face(img)
        if faces is None:
            return lst_id

        face_recognizer = cv.face.LBPHFaceRecognizer_create()
        labels = [i for i in range(len(student_faces))]
        face_recognizer.train(student_faces, np.array(labels))

        for i in range(len(faces)):
            f = faces[i]
            label = face_recognizer.predict(f)
            print("label", str(label))
            if (label is None):
                continue
            lst_id.append(label)
            lst_grid.append(face_rect[i])


        return lst_id,lst_grid

    def get(self, request, *args, **kwargs):
        at_id = self.kwargs['at_id']
        StudentAttendance.objects.filter(attendance__id=at_id).delete()

        img_objects = DJImage.objects.filter(attendance__id=at_id, attendance__class_room__instructor = self.request.user)
        print("Hello ")

        student_faces=[]
        student_ids=[]
        mp_id_to_name={}
        if len(img_objects) > 0:
            class_ = img_objects[0].attendance.class_room
            att =  img_objects[0].attendance
            all_class_student_in_class = ClassStudent.objects.filter(classroom=class_)
            for class_student in all_class_student_in_class:
                student_ids.append(class_student.student.id)
                student = AppUser.objects.get(user__id = class_student.student.id)
                student_image=cv.imread(os.getcwd()+os.path.sep+student.picture.url)
                student_faces.append( cv.cvtColor(student_image, cv.COLOR_BGR2GRAY) )
                mp_id_to_name[student.user.id] = student.user.first_name




        mp_tuple ={}
        k=0
        lst_img=[]
        prc_img_path_list = []
        for img_object in img_objects:
            path = img_object.img.url
            original_image = cv.imread(os.getcwd() + path)
            print(os.getcwd() + path)
            img = original_image.copy()
            lst_img.append(img)
            processed_img_path = img_object.processed_img.url
            if processed_img_path is None:
                base_path = os.path.basename(path)
                processed_img_path = "processed_"+base_path
            prc_img_path_list.append(processed_img_path)

            lst_t, lst_grid=self.doSomething(img, student_faces, student_ids)
            for i in range(len(lst_t)):
                indx,th = lst_t[i]
                #Student ID
                id = student_ids[indx]

                if id not in mp_tuple.keys():
                    mp_tuple[id]= (k, th, lst_grid[i])
                elif th > mp_tuple[id][1]:
                    mp_tuple[id] = (k, th, lst_grid[i])


            k=k+1

        for id,value in mp_tuple.items():
            k,th, rect = value
            x = rect[0]
            y = rect[1]

            self.draw_text(lst_img[k], mp_id_to_name[id] + " " + str(round(th, 1)), x, y)
            self.draw_rectangle(lst_img[k], rect)
            cv.imwrite(os.getcwd()+prc_img_path_list[k], lst_img[k])
            print("Image Saved")


        lst_ids = mp_tuple.keys()

        if len(img_objects) > 0:
            i = 0
            class_ = img_objects[0].attendance.class_room
            att =  img_objects[0].attendance
            all_class_student_in_class = ClassStudent.objects.filter(classroom=class_)
            for class_student in all_class_student_in_class:
                if class_student.student.id in lst_ids:
                    s = StudentAttendance(attendance=att, student=class_student.student)
                    i+=1
                    s.save()

            att.num_present=i
            att.save()
        print("World")
        return Response(status=status.HTTP_200_OK)


class InitAttendanceView(APIView):
    def get(self, request, *args, **kwargs):
        print("init")
        return Response(status=status.HTTP_200_OK)







from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token

class CustomObtainAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        response = super(CustomObtainAuthToken, self).post(request, *args, **kwargs)
        token = Token.objects.get(key=response.data['token'])
        return Response({'token': token.key, 'id': token.user_id})