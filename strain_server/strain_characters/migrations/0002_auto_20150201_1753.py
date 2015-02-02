# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('strain_characters', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='character',
            name='money',
            field=models.IntegerField(default=200),
        ),
    ]
