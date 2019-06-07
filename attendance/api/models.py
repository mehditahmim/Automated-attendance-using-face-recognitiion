from django.db import models

from django.db.models.signals import post_save
from django.contrib.auth.models import User
from rest_framework.authtoken.models import Token
from django.dispatch import receiver

@receiver(post_save, sender=User)
def create_auth_token(sender, instance=None, created=False, **kwargs):
    if created:
        Token.objects.create(user=instance)

class AppUser(models.Model):
    user = models.OneToOneField(User,on_delete=models.CASCADE)
    is_student = models.BooleanField(default=False)
    picture = models.ImageField(upload_to='images/',blank=True, max_length=255, null=True)

class ClassRoom(models.Model):
    name = models.CharField(max_length=255, blank=False)
    instructor = models.ForeignKey('auth.User', related_name='instructor', on_delete=models.CASCADE)
    course_no = models.CharField(max_length=255, blank=False)
    date_created = models.DateTimeField(auto_now_add=True)

class ClassStudent(models.Model):
    classroom = models.ForeignKey(ClassRoom, on_delete=models.CASCADE, null=True, related_name="student_c")
    student = models.ForeignKey(User, on_delete=models.CASCADE,null=True, related_name="c_student")

class Attendance(models.Model):
    class_room = models.ForeignKey(ClassRoom, on_delete=models.CASCADE, null=True, related_name="at_class")
    date_created = models.DateTimeField(auto_now_add=True)
    num_present = models.IntegerField(default=0)

class DJImage(models.Model):
    attendance = models.ForeignKey(Attendance, on_delete=models.CASCADE, null=True, related_name="image_attendance")
    img = models.ImageField(upload_to='at_images/', blank=True, max_length=255, null=True)
    processed_img = models.ImageField(upload_to='pro_images/', blank=True, max_length=255, null=True)

class StudentAttendance(models.Model):
    attendance = models.ForeignKey(Attendance,on_delete=models.CASCADE, null=True,related_name="student_attendance")
    student = models.ForeignKey(User, on_delete=models.CASCADE, null=True)



