# api/serializers.py

from rest_framework import serializers
from .models import *
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = DJImage
        fields = ('id', 'img','processed_img','attendance')

class StudentAttendanceSerializer(serializers.ModelSerializer):
    class Meta:
        model = StudentAttendance
        fields = ('id', 'attendance', 'student')

class ClassStudentSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClassStudent
        fields = ('id', 'classroom', 'student')

class AttendanceSerializerExtended(serializers.ModelSerializer):
    attendance_image= ImageSerializer(source="image_attendance", many=True)
    attendance_student = StudentAttendanceSerializer(source="student_attendance", many=True)

    class Meta:
        model = Attendance
        fields = ('id', 'class_room','num_present','date_created','attendance_image','attendance_student')
        read_only_fields = ('date_created',)

class AttendanceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Attendance
        fields = ('id', 'class_room', 'date_created','num_present')
        read_only_fields = ('date_created',)




class ClassRoomSerializer(serializers.ModelSerializer):
    instructor_name = serializers.ReadOnlyField(source='instructor.username')
    class_student = ClassStudentSerializer(source="student_c", many=True)
    class_attendance = AttendanceSerializer(source="at_class", many=True)

    class Meta:
        model = ClassRoom
        fields = ('id', 'name','instructor_name','course_no', 'date_created','class_attendance','class_student')
        read_only_fields = ('date_created',)

class CreateClassRoomSerializer(serializers.ModelSerializer):
    instructor_name = serializers.ReadOnlyField(source='instructor.username')
    class Meta:
        model = ClassRoom
        fields = ('id', 'name','instructor_name','course_no', 'date_created')
        read_only_fields = ('date_created',)


class UserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)
    is_student = serializers.BooleanField(default=False, source="appuser.is_student")
    picture = serializers.ImageField(source="appuser.picture")

    class Meta:
        model = User
        fields = ('id','username', 'first_name', 'last_name', 'password', 'is_student','picture')
        # fields = ('id', 'username', 'first_name', 'last_name', 'password', 'is_student')

    def create(self, validated_data, instance=None):
        profile_data = validated_data.pop('appuser')
        user = User.objects.create(**validated_data)
        user.set_password(validated_data['password'])
        print("password", validated_data['password'])
        user.save()
        AppUser.objects.update_or_create(user=user,**profile_data)
        return user

class UserSerializer2(serializers.ModelSerializer):
    is_student = serializers.BooleanField(default=False, source="appuser.is_student")
    picture = serializers.ImageField(source="appuser.picture")
    class Meta:
        model = User
        # fields = ('id','username', 'first_name', 'last_name', 'password', 'is_student','picture')
        fields = ('id', 'username',  'password', 'is_student','picture')


class LandingPageSerializer(serializers.ModelSerializer):
    classrooms = serializers.SerializerMethodField('_classrooms')
    is_student = serializers.BooleanField(default=False, source="appuser.is_student")
    picture = serializers.ImageField(source="appuser.picture")

    def _classrooms(self, obj):
        user_id = self.context['request'].user.id
        is_student_list = AppUser.objects.filter(user__id=user_id).values_list('is_student', flat=True)


        if is_student_list[0] == True:
            print("I am a Student")
            queryset = ClassStudent.objects.filter(student__id=user_id).values_list('classroom', flat=True)
            return queryset

        queryset = ClassRoom.objects.filter(instructor__id=user_id).values_list('id', flat=True)
        return queryset

    class Meta:
        model = User
        fields = ('id','username', 'first_name', 'last_name', 'is_student', 'picture', 'classrooms')
