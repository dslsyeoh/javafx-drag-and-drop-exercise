/*
 * Author Steven Yeoh
 * Copyright (c) 2020. All rights reserved
 */

package com.dsl.drag.and.drop.exercise;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application
{
    private ConfigurableApplicationContext context;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init() throws Exception
    {
        context = SpringApplication.run(Main.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent parent = ContextProvider.load(getClass().getResource("/fxml/Draggable.fxml"));

        Scene scene = new Scene(parent, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception
    {
        context.stop();
    }
}
