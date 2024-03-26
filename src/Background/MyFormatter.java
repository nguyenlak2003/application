
package Background;

import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.NumberFormatter;

public class MyFormatter extends NumberFormatter
{

    public MyFormatter(NumberFormat format)
    {
        super(format);
    }

    
    public Object stringToValue(String text) throws ParseException
    {
        if("".equals(text))
            return 0;
        
        return super.stringToValue(text);
    }
    
}
