package Exceptions;

/**
 * Created by stili on 10/24/2016.
 */
public class AuthenticationError extends RuntimeException
{
    public AuthenticationError(String message)
    {
        super(message);
    }
}