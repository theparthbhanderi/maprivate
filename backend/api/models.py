from django.db import models
from django.contrib.auth.models import User
import uuid

class ImageProject(models.Model):
    PROCESSING_TYPES = [
        ('colorize', 'Colorize'),
        ('restore', 'Restore'),
        ('upscale', 'Upscale'),
    ]
    STATUS_CHOICES = [
        ('pending', 'Pending'),
        ('processing', 'Processing'),
        ('completed', 'Completed'),
        ('failed', 'Failed'),
    ]

    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True, blank=True) # Check if we want to enforce it now or later. Let's allowing null for dev transition.
    original_image = models.ImageField(upload_to='originals/')
    processed_image = models.ImageField(upload_to='processed/', null=True, blank=True)
    processing_type = models.CharField(max_length=20, choices=PROCESSING_TYPES, default='restore')
    settings = models.JSONField(default=dict, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='pending')

    def __str__(self):
        return f"{self.processing_type} - {self.id}"
