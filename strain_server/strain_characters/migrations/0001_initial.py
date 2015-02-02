# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('user', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Character',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('position', models.CharField(max_length=256)),
                ('active', models.BooleanField(default=False)),
                ('maxHealth', models.IntegerField(default=1)),
                ('health', models.IntegerField(default=1)),
                ('strength', models.IntegerField(default=1)),
                ('accuracy', models.IntegerField(default=1)),
                ('stamina', models.IntegerField(default=1)),
                ('perception', models.IntegerField(default=1)),
                ('money', models.IntegerField(default=1)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='CharacterClass',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('name', models.CharField(max_length=32)),
                ('health', models.IntegerField(default=1)),
                ('strength', models.IntegerField(default=1)),
                ('accuracy', models.IntegerField(default=1)),
                ('stamina', models.IntegerField(default=1)),
                ('perception', models.IntegerField(default=1)),
                ('weaponTypes', models.CharField(max_length=256)),
                ('armourTypes', models.CharField(max_length=256)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Faction',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('name', models.CharField(max_length=32)),
                ('description', models.CharField(max_length=256)),
                ('best_score', models.IntegerField(default=0)),
                ('total_score', models.IntegerField(default=0)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='character',
            name='charClass',
            field=models.ForeignKey(to='strain_characters.CharacterClass'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='character',
            name='faction',
            field=models.ForeignKey(to='strain_characters.Faction'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='character',
            name='user',
            field=models.ForeignKey(to='user.User'),
            preserve_default=True,
        ),
    ]
