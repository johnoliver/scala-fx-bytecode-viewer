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
import javafx.fxml.Initializable
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.event.ActionEvent
import org.apache.bcel.classfile.ClassParser
import org.apache.bcel.classfile.Method
import org.apache.bcel.generic.InstructionList
import java.io.File
import javafx.scene.layout.HBox
import javafx.scene.control.TreeView
import javafx.scene.control.TextArea
import javafx.scene.control.MenuItem
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.stage.FileChooser
import scala.collection.JavaConversions
import javafx.application.Platform
import javax.swing.JOptionPane

class BytecodeViewerUI extends Initializable {

  @FXML
  var bytecodeTree: TreeView[TreeNodes] = null;
  var bytecodeView: BytecodeView = null;

  @FXML
  var srcView: TextArea = null;
  var sourceView: SourceView = null;

  @FXML
  var openFile: MenuItem = null

  @FXML
  var exit: MenuItem = null

  class OptionSelect(selected: () => Unit) extends EventHandler[ActionEvent] {
    def handle(event: ActionEvent): Unit = {
      event getEventType match {
        case e if (e == ActionEvent.ACTION) => {
          //got a selected action
          selected()
        }
        case _ => {
        }
      }
    }
  }

  val menuSelect = new OptionSelect(() => {
    val sourceFile = openFileDialog("Please select source file", "Source Files (*.java|*.scala)", List("*.java", "*.scala"))
    if (sourceFile isDefined) {
      val classFile = openFileDialog("Please select class file", "Class File (*.class)", List("*.class"))
      displayFiles(sourceFile, classFile);
    }
  })

  val exitSelect = new OptionSelect(() => {
    Platform exit;
  })

  def displayFiles(src: Option[File], classFile: Option[File]): Unit = {
    if (src.isDefined && classFile.isDefined) {
      sourceView setText (src.get);
      bytecodeView setBytecode (classFile.get);
    }
  }

  def openFileDialog(title: String, extentionName: String, extentions: List[String]): Option[File] = {
    val fileChooser = new FileChooser();

    fileChooser setTitle (title);

    //Set extension filter
    val extFilter = new FileChooser.ExtensionFilter(extentionName, JavaConversions.seqAsJavaList(extentions));
    fileChooser.getExtensionFilters() add (extFilter);

    //Show open file dialog
    val sourceFile = fileChooser showOpenDialog (null);

    sourceFile match {
      case null =>
        JOptionPane showMessageDialog (null, "No file was selected."); return Option.empty
      case f if !f.exists() =>
        JOptionPane showMessageDialog (null, "File does not exist."); return Option.empty
      case _ => return Some(sourceFile);
    }

  }

  override def initialize(url: URL, bundle: ResourceBundle) = {
    sourceView = new SourceView(srcView);

    bytecodeView = new BytecodeView(bytecodeTree, sourceView)

    openFile setOnAction (menuSelect);
    exit setOnAction (exitSelect);

  }
}