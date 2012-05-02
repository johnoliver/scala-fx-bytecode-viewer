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
import javafx.scene.layout.VBox
import javafx.fxml.FXML
import javafx.event.ActionEvent
import org.apache.bcel.classfile.Method
import org.apache.bcel.classfile.Code
import org.apache.bcel.util.ByteSequence
import javafx.scene.control.TreeView
import javafx.scene.control.TreeItem
import org.apache.bcel.classfile.ClassParser
import scala.collection.JavaConversions
import java.io.File
import javafx.beans.property.ObjectProperty
import javafx.fxml.Initializable
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.layout.HBox
import javafx.scene.control.TextArea
import javafx.scene.Cursor
import javafx.application.Platform
import javafx.scene.input.ScrollEvent
import javafx.event.EventHandler

class SourceView(srcView: TextArea) {

  var text: String = "";
  var lines: Seq[String] = Seq.empty;

  srcView.setEditable(false);

  def setText(file: File) = {

    val source = scala.io.Source.fromFile(file)
    text = source mkString;
    source close;

    lines = text split ("\n");

    srcView setText (text);
  }

  def setPosition(pos: Int) {

    val start = lines.take(pos - 1).map(_.length).sum + pos - 1;
    val end = start + lines(pos - 1).length();

    Platform.runLater(new Runnable() {
      def run(): Unit = {
        srcView.selectRange(start, end);
      }
    })
  }

}
