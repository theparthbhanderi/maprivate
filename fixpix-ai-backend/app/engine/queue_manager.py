import asyncio
from fastapi import HTTPException
import logging

logger = logging.getLogger(__name__)

class JobQueue:
    """
    Manages concurrent access to the AI engine.
    Uses a Semaphore to limit parallel executions.
    """
    def __init__(self, max_concurrent=1):
        self.semaphore = asyncio.Semaphore(max_concurrent)
        self.max_concurrent = max_concurrent
        
    async def run_job(self, func, *args, **kwargs):
        """
        Submit a job to the execution queue.
        If busy, waits (fair lock).
        """
        if self.semaphore.locked():
            logger.info("Job Queue busy, waiting for slot...")
            
        async with self.semaphore:
            logger.info("Job slot acquired. Running inference.")
            try:
                # Ensure the function is awaitable or wrap it
                if asyncio.iscoroutinefunction(func):
                   return await func(*args, **kwargs)
                else:
                   # If sync function, run in threadpool to avoid blocking event loop
                   # but we hold the semaphore so logic is serialized
                   from fastapi.concurrency import run_in_threadpool
                   return await run_in_threadpool(func, *args, **kwargs)
            except Exception as e:
                logger.error(f"Job execution failed: {e}")
                raise e
            finally:
                logger.info("Job finished. Slot released.")

# Global instance
# We set max_concurrent=1 (safe for single GPU/CPU)
job_queue = JobQueue(max_concurrent=1)
