from django.conf.urls import patterns, url

from strain_characters import views

urlpatterns = patterns('',
    # /character
    url(r'^$', views.index, name='index'),
    # ex: /character/dosaki/
    #url(r'^setup/$', views.setup, name='setup'),
    url(r'^register/$', views.register, name='register'),
    url(r'^character/(?P<name>\w+)/$', views.username, name='username'),
    url(r'^class/(?P<className>\w+)/$', views.classData, name='classData'),
)
