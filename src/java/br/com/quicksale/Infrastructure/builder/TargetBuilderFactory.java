package br.com.quicksale.Infrastructure.builder;


/**
 *
 * @author jean.souza
 */
public class TargetBuilderFactory {
    
    private static TargetBuilder builder;
    
    private TargetBuilderFactory()
    {
        
    }      
    
     
    public static String buildTarget(String target)
    {
        if(builder == null) {
            builder = new TargetBuilder();
        }
        
        return builder.generateTarget(target);
    }
    
   
}
