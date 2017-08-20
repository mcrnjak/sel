package com.milancrnjak.sel.function.impl;

import com.milancrnjak.sel.function.Function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Date function. Can be called without any argument and then it returns the current date.
 * It can also be called with two arguments - date string and date format and in that case
 * it parses the date string and returns a corresponding Date object.
 */
public class DateFunction implements Function {

    @Override
    public Object execute(List<Object> args) {
        if (args == null || args.size() == 0) {
            return new Date();
        }

        if (args.size() == 2) {
            String dateStr = (String) args.get(0);
            String dateFormat = (String) args.get(1);

            try {
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                return formatter.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Date function accepts zero or two arguments");
    }
}
