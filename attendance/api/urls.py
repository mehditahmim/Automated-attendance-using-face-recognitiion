# api/urls.py

from django.conf.urls import url, include
from rest_framework.urlpatterns import format_suffix_patterns
from .views import *
from rest_framework.authtoken.views import obtain_auth_token

urlpatterns = {
    url(r'^auth/', include('rest_framework.urls', namespace='rest_framework')),

    url(r'^classroom/$', CreateClassRoomView.as_view(), name="create"),
    url(r'^classlist/$', ListClassRoomView.as_view(), name="listclass"),
    url(r'^sclasslist/$', SListClassRoomView.as_view(), name="listclass"),
    url(r'^classroom/(?P<pk>[0-9]+)/$', DetailsClassRoomView.as_view(), name="details"),
    url(r'^attendance/$', CreateAttendanceView.as_view(), name="attendance"),
    url(r'^at-image/$', CreateAttendanceImageView.as_view(), name="image attendance"),
    url(r'^landingpage/(?P<pk>[0-9]+)/$', LandingPageView.as_view(), name="teacher details"),
    url(r'^createusers/$', CreateUserView.as_view(), name="create user"),
    url(r'^users/$', AllUserView.as_view(), name="all user"),
    url(r'^get-token/', CustomObtainAuthToken.as_view()),
    url(r'^log-out/', Logout.as_view()),
    url(r'^joinclass/$', CreateJoinClassView.as_view(), name="join class"),
    url(r'^all-at/(?P<class_id>.+)/$', DayAttendanceView.as_view(), name="attendance day"),
    url(r'^att/(?P<at_id>.+)/$', DetailsAtView.as_view(), name="attendance view"),
    url(r'^calc-at/(?P<at_id>.+)/$', CalcAttendanceView.as_view(), name="attendance calc"),
    url(r'^init/$', InitAttendanceView.as_view(), name="init attendance"),
}

urlpatterns = format_suffix_patterns(urlpatterns)