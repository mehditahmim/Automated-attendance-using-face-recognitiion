# Generated by Django 2.2 on 2019-04-27 03:26

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0013_auto_20190426_1708'),
    ]

    operations = [
        migrations.AlterField(
            model_name='classroom',
            name='instructor',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='alu', to=settings.AUTH_USER_MODEL),
        ),
    ]