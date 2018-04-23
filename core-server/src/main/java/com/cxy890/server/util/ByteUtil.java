package com.cxy890.server.util;

import lombok.Cleanup;
import sun.misc.BASE64Decoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;

/**
 * @author BD-PC27
 */
public class ByteUtil {

    public static byte[] getIcon() throws IOException {
        try {
            return image2byte("favicon.ico");
        } catch (Exception e) {
            BASE64Decoder decoder = new BASE64Decoder();
            return decoder.decodeBuffer(icon);
        }
    }

    private static final String icon = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAE+0lEQVRYhcWXf2hVZRjHP8/ZVeZay9mwuY1poVM2yMl0nXO3e/2RMpM0+2P7w4qQ6K8gEEwKqegPkTAqo4Lsp0UQRRQaSmYLt7Z7p5uhqxRbxrrXMce0Mda4u7v3ffrjnF33606nq75wOPd934fv8z3ned/nfK9wC6h27DxLZBNQBljAb6p6pDEUjt4oh9xM4oBtW2LJ08CLwFxVvUYoMgi8boy+9GM4HJ9xAdVlZVh35OwDdqpLcAGoBxJANVDuhX6WNPpYUzicmIovYzrJa2trGYwPbQTeBFTgBdBtDc3hw52R6NGCBQvezbCsP4EHgOWWyKWk0tbf3z8zAgrz8kga846q3iMiHw4PJ3Y2tZw0I+vRS126sKDwJywBWAcszZmb+3YkEtF0nNZ0BMSG4zlApYgYYH/o1KkJMQ0tLQAHgBhQYpnkoqk4pyUAZB6QBcRUuZiWdDjRA1wFyLCs/JUrV86QAKEfiAOzRchLF2Z8GdlANoAxGsuaPWvjjAhIJE0f8CvgA7ZPFlNbWwsidUAO0I1QDmyvrKxML+C+FSsIOPai6wkoKi42wFvecFfQ72xZv2ZNan392rVcvhS1gX3e1HvADsDO9GX4JuMUgA0bNjD098DLIuw+0RSa8tz6V63y+Wb5vgIexD37hwS+Vff3aqAO1UxEWoH9wCeAAZY2NIc6xvNlAAwODnJnbu5mYF5nJHp2KgGRri6zsKjoMCJFwHJVLUVkM/CQqi4XEQuRo6rUifAqUOQ9aFtSOTO+J1gA3d3dAGeA54N+J3MqAQANofDAsEk+Djgi8gZwHKgXkQNADcZsFuFeYHThA8XFxRO4UnVR1VYRWQxsAz64nohQ+CTASe8ag2CVbaHsZuwmr85IJCzccqSQChCj54E+4JmA3z/phrlhqGwFguNmS8Rn5Y8PTQnounIljvs0ywTdcrO5g46dB7zGxCNugdhpBXR0dAB87w13BBxnml0SVlc5PkTeByYW20WgpqZmcgEe6r27X4T0/XMSVJSXW6rsBaZ6e/bVrq70Aoyan4EuVbWAp240+doqv3Vb1pw9wM5xSx3A6Iylc27Pzk4rIPHLuThwXEQAtgb8Ttp+P4KgY2cnVQ8Cz45bOiTgAM+NmstRZVFaAWG3SRweCRZ4NF3iasch6HdsRFoYG9cDbM9QHj7RHOpF9Wvc0wWACKVpBQCgehwY9EZP3m/bY2KqqqoI+p0SSzgINEKKsA94BSi7q7Doox9CIQPQEAr3A8dcagUoGc034bzHksm+TJ+vHrfXlw5bYgPNAb+dJcg61DwBbPLER4EG4DvgSHtr29W/4pP60C+BOq+0d1dUVNDW1ja5AGMU4AtPAMDeoN/pwW2rUeA08AjQatRE9fc/Ek09PZMlTUGVY+J6iRygYCQ5pHHFAcfOE5FOXPczHglcUzKA63p6vXsP0CPQqXBekXBjc3PqdQT9zqequk1ETi+bN1Rx4JvTQBpDEu2+3Itbz17c/RDzrj6g30vY54kY8OZ6cZNfEKGDxPD4z/pBrwR550cdzLT/C+bPn0/p4sW+JOrD87Q+yzJqkqY/NmSS8bjJLyoiEomwZMkS2tvbuXgxrU0k4Pf7BD0H5EvS5J5oaUlMKeDfQNDv7AL2qGphYyjcA9N2xbcGVf0YiIlQMDL3nwpoDIW7gc9B/h8BHvbDNUt/a8bjJqBGzyLERsb/AKulzmJdLC+gAAAAAElFTkSuQmCC";

    /**
     * 图片到byte数组
     *
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] image2byte(String file) throws IOException {
        @Cleanup FileImageInputStream input = new FileImageInputStream(new File(file));
        @Cleanup ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numBytesRead;
        while ((numBytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, numBytesRead);
        }
        return output.toByteArray();
    }

}
