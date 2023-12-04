package me.phoenixra.atumodcore.api.misc;

import me.phoenixra.atumodcore.api.AtumMod;

import java.util.function.Consumer;

public class AtumDebugger {
    private final AtumMod atumMod;
    private final String taskId;
    private final String startInfo;
    private Consumer<Exception> onError = null;
    /**
     * Task Debugger, which can help u to
     * check the time required to execute the task
     *
     * @param atumMod the mod
     * @param taskId id of a task
     * @param startInfo info that is written on start debugging
     */
    public AtumDebugger(AtumMod atumMod, String taskId, String startInfo){
        this.atumMod = atumMod;
        this.taskId = taskId;
        this.startInfo = startInfo;
    }

    /**
     * Set the error handler
     *
     * @param onError the error handler
     * @return the debugger
     */
    public AtumDebugger onError(Consumer<Exception> onError){
        this.onError = onError;
        return this;
    }

    /**
     * Start the task
     *
     */
    public void start(Runnable taskToDebug){
        if(!atumMod.isDebugEnabled()){
            try {
                taskToDebug.run();
                return;
            }catch (Exception exception){
                if(onError != null){
                    onError.accept(exception);
                }
            }
        }
        atumMod.getLogger().info(
                "DEBUG-> task with id "+taskId+" has been started\n"+startInfo);
        long debugTimer = System.currentTimeMillis();
        try{
            taskToDebug.run();
        }catch (Exception exception){
            atumMod.getLogger().info("DEBUG-> task with id "+taskId+" FAILED WITH AN ERROR!");
            exception.printStackTrace();
            if(onError != null){
                onError.accept(exception);
            }
        }
        debugTimer = System.currentTimeMillis() - debugTimer;
        atumMod.getLogger().info("DEBUG-> task with id "+taskId+" SUCCESS\n " +
                "It took "+debugTimer+" milliseconds to execute");

    }

}
