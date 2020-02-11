/*
 * Author Steven Yeoh
 * Copyright (c) 2020. All rights reserved
 */

package com.dsl.drag.and.drop.exercise;

import lombok.Data;
import org.tbee.javafx.scene.layout.fxml.MigPane;

@Data
public class CustomNode extends MigPane
{
    private int groupId;
    private boolean isRoot;
}
