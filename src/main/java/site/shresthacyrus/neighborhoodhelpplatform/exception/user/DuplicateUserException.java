package site.shresthacyrus.neighborhoodhelpplatform.exception.user;

/* Two ways to create exception:
Checked and Unchecked
Checked expression: compiler checks if it is handled or not
    How it handles: throw or try/catch
Unchecked expression: compiler doesn't check if it is handled
    extends RuntimeException
 */
public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message){
        super(message); // To return the message when we call, e.getMessage
    }
}
