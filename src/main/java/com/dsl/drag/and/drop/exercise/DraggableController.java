/*
 * Author Steven Yeoh
 * Copyright (c) 2020. All rights reserved
 */

package com.dsl.drag.and.drop.exercise;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.MigPane;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("draggableController")
public class DraggableController implements Initializable
{
    @FXML
    private MigPane dragContainer;

    @FXML
    private CustomNode source;

    @FXML
    private CustomNode source2;

    @FXML
    private CustomNode source3;

    @FXML
    private MigPane target;

    private final DataFormat IS_ROOT = new DataFormat("isRoot");
    private final DataFormat ROOT_TEXT = new DataFormat("rootText");
    private final DataFormat CHILD_TEXT = new DataFormat("childText");

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        dragContainer.getChildren().stream().map(CustomNode.class::cast).forEach(node -> {
            setOnDragDetected(node);
            setOnDragDone(node);
        });
        setOnDragOver();
        setOnDragDropped();
    }

    private void setOnDragDetected(CustomNode node) {

        node.setOnDragDetected(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(getText(node));
            content.put(IS_ROOT, node.isRoot());

            for (Node value : dragContainer.getChildren())
            {
                CustomNode customNode = (CustomNode) value;
                if (!Objects.equals(customNode, node) && Objects.equals(node.getGroupId(), customNode.getGroupId()))
                {
                    if (customNode.isRoot())
                    {
                        node.add(0, customNode, "growx");
                        content.put(ROOT_TEXT, getText(customNode));
                    }
                    else
                    {
                        content.put(CHILD_TEXT, getText(customNode));
                        node.add(customNode, "growx");
                    }
                    break;
                }
            }

            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(getSnapshot(node), event.getX(), event.getY());
            db.setContent(content);
            event.consume();

        });
    }

    private String getText(CustomNode node)
    {
        return ((Label) node.getChildren().get(0)).getText();
    }

    private void setOnDragOver()
    {
        target.setOnDragOver(event -> {
            if(!Objects.equals(event.getGestureSource(), target))
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    private void setOnDragDropped()
    {
        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if(Objects.equals(event.getGestureTarget(), target))
            {
                if(!Boolean.parseBoolean(db.getContent(IS_ROOT).toString()))
                {
                    target.add(create(db.getContent(ROOT_TEXT).toString()), "growx");
                    target.add(create(db.getString()), "growx");
                }
                else
                {
                    target.add(create(db.getString()), "growx");
                    target.add(create(db.getContent(CHILD_TEXT).toString()), "growx");
                }
                db.clear();
                event.setDropCompleted(true);
            }
            else
            {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private void setOnDragDone(CustomNode node)
    {
        node.setOnDragDone(event -> node.setVisible(false));
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
