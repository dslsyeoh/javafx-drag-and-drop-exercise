/*
 * Author Steven Yeoh
 * Copyright (c) 2020. All rights reserved
 */

package com.dsl.drag.and.drop.exercise;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component("draggableController")
public class DraggableController implements Initializable
{
    @FXML
    private MigPane source;

    @FXML
    private MigPane source2;

    @FXML
    private MigPane target;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Stream.of(source, source2).forEach(node -> {
            setOnDragDetected(node);
            setOnDragDone(node);
        });
        setOnDragOver();
        setOnDragDropped();
    }

    private void setOnDragDetected(MigPane node) {

        node.setOnDragDetected(event -> {
            System.out.println("onDragDetected");
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(getSnapshot(node), event.getX(), event.getY());
            ClipboardContent content = new ClipboardContent();
            content.putString(((Label) node.getChildren().get(0)).getText());
            db.setContent(content);
            event.consume();
        });
    }

    private void setOnDragOver()
    {
        target.setOnDragOver(event -> {
            System.out.println("onDragOver");
            if(!Objects.equals(event.getGestureSource(), target) && event.getDragboard().hasString())
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    private void setOnDragDropped()
    {
        target.setOnDragDropped(event -> {
            System.out.println("onDragDropped");
            Dragboard db = event.getDragboard();
            if(Objects.equals(event.getGestureTarget(), target))
            {
                target.add(create(db.getString()), "growx");
                event.setDropCompleted(true);
            }
            else
            {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private void setOnDragDone(MigPane node)
    {
        node.setOnDragDone(event -> {
            System.out.println("onDragDone");
            node.setVisible(false);
        });
    }

    private WritableImage getSnapshot(Node node)
    {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        return node.snapshot(snapshotParameters, null);
    }

    private Label create(String text)
    {
        Label label = new Label(text);
        label.getStyleClass().addAll("frame", "padding");

        return label;
    }
}
