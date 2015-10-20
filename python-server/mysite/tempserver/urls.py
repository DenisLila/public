from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.temp_page),
    url(r'temp_page$', views.temp_page),
    url(r'get_temp$', views.get_temp_req),
]
