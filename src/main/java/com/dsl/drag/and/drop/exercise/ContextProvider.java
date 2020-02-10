package com.dsl.drag.and.drop.exercise;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component("contextProvider")
public class ContextProvider implements ApplicationContextAware
{
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        ContextProvider.context = context;
    }

    public static <T> T load(URL url) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);

        return loader.load();
    }

    public static <T> T load(URL url, Object controller) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        return loader.load();
    }
}
