from rest_framework.permissions import BasePermission
from .models import *
from django.contrib.auth.models import User

class IsOwner(BasePermission):
    """Custom permission class to allow only bucketlist owners to edit them."""

    def has_object_permission(self, request, view, obj):
        """Return True if permission is granted to the bucketlist owner."""
        if isinstance(obj, ClassRoom):
            return obj.instructor == request.user
        elif isinstance(obj, User):
            return obj == request.user
        elif isinstance(obj, Attendance):
            return obj.class_room.instructor == request.user
        elif isinstance(obj, DJImage):
            return obj.attendance.class_room.instructor == request.user



        return obj.instructor == request.user