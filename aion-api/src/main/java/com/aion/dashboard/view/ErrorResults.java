package com.aion.dashboard.view;

public class ErrorResults {
    private ErrorResults(){
        throw new UnsupportedOperationException("Cannot create an instance of: "+ErrorResults.class.getSimpleName());
    }

    //Errors should always return an explanation of the error and the matching http code
    public static final ResultInterface SERVER_ERROR = Result.from(500,"Error: Server Error");
    public static final ResultInterface NOT_FOUND = Result.from(404, "Error: Resource not found");
    public static final ResultInterface MISSING_ARGUMENT=Result.from(400, "Error: Missing argument");
    public static final ResultInterface ARGUMENT_PARSING_ERROR = Result.from(400, "Error: Could not parse argument.");
    public static final ResultInterface incorrectArgument(String message){
        return Result.from(400, "Error: Incorrect Argument. Expected: " + message);
    }

}
