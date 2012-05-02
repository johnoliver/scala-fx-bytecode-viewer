/* Copyright 2012 John Oliver

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.insightfullogic.oss.bytecode

import java.lang.Math.random
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Application
import javafx.beans.value.WritableValue
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.effect.BlendMode
import javafx.scene.effect.BoxBlur
import javafx.scene.paint.Color._
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.StrokeType
import javafx.stage.Stage
import javafx.util.Duration
import scala.collection.JavaConversions._
import javafx.scene.Parent
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import java.util.ResourceBundle
import java.net.URL
import javafx.scene.paint.Color
import javafx.scene.control.MenuBar
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem

class BytecodeViewerApp extends Application {
  override def start(stage: Stage) {
    val root: Parent = FXMLLoader load (getClass() getResource ("BytecodeViewerUI.fxml"));

    val scene = new Scene(root);

    stage setScene (scene);
    stage show ();
  }
}

object Main {
  def main(args: Array[String]) {
    Application launch (classOf[BytecodeViewerApp])
  }
}
