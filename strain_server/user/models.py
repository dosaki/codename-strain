from django.db import models

# Create your models here.
class User(models.Model):
    username = models.CharField(max_length=32)
    email = models.CharField(max_length=256)

    def __str__(self):
        return self.username
