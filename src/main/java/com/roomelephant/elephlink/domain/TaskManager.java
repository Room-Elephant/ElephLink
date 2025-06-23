package com.roomelephant.elephlink.domain;

/**
 * The {@code TaskManager} interface defines the contract for managing and executing scheduled tasks.
 * Implementations of this interface are responsible for validating cron expressions,
 * scheduling tasks for execution, and cleanly shutting down the task management service.
 */
public interface TaskManager {
  /**
   * Validates a cron expression.
   * This method typically checks if the cron expression provided during the manager's
   * initialization or configuration is syntactically correct and can be parsed.
   *
   * @return {@code true} if the cron expression is valid, {@code false} otherwise.
   */
  boolean validateCronExpression();

  /**
   * Executes a given task after a specified delay.
   * The task will be executed once after the initial delay. For recurring tasks
   * based on a cron expression, the implementation should handle subsequent scheduling
   * internally after the initial execution.
   *
   * @param task  the {@link Runnable} task to be executed.
   * @param delay the delay in milliseconds before the task is first executed.
   */
  void execute(Runnable task, long delay);

  /**
   * Initiates an orderly shutdown of the task manager.
   * This method attempts to gracefully terminate all actively running and pending tasks.
   * It should allow currently executing tasks to complete, but will not accept new tasks.
   * Implementations should typically include a timeout to force shutdown if tasks do not
   * complete within a reasonable period.
   */
  void shutdown();
}
