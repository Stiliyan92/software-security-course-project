package Exceptions;

/**
 * Created by stili on 10/22/2016.
 */
public class UndefinedAccount extends RuntimeException
{
    public UndefinedAccount(String message)
    {
        super(message);
    }
}
