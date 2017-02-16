package Exceptions;

/**
 * Created by stili on 10/22/2016.
 */

public class LockedAccount extends RuntimeException
{
    public LockedAccount(String message)
    {
        super(message);
    }
}
