from django.conf.urls import patterns, include, url
from django.contrib import admin
from user import views
from strain_characters import views

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'strain_server.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^', include('strain_characters.urls')),
    url(r'^admin/', include(admin.site.urls)),
)
