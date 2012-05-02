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
import javafx.event.EventType
import javafx.event.EventHandler
import java.io.Serializable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.apache.bcel.classfile.JavaClass

sealed trait TreeNodes {
  def displayValue: String;
  override def toString(): String = {
    displayValue;
  }
}

class LineAndCode(line: Int, inst: Instruction, meth: Method) extends TreeNodes {
  def displayValue = inst toString;
}

class MethodNode(meth: Method) extends TreeNodes {
  def displayValue = meth getName
}

class ClassNode(clazz: JavaClass) extends TreeNodes {
  def displayValue = clazz getClassName
}

class BytecodeView(treeView: TreeView[TreeNodes], srcView: SourceView) {

  var clazz: File = null

  val listener = new ChangeListener[TreeItem[TreeNodes]]() {
    def changed(x: ObservableValue[_ <: TreeItem[TreeNodes]], old: TreeItem[TreeNodes], newS: TreeItem[TreeNodes]): Unit = {
      sourceCodeLookup get (newS getValue) foreach (srcView setPosition (_))
    }
  }

  treeView.getSelectionModel().selectedItemProperty().addListener(listener)
  val sourceCodeLookup = scala.collection.mutable.Map[TreeNodes, Int]();

  def setBytecode(file: File) = {
    if (file != null) {
      clazz = file;
      if (treeView != null)
        treeView setRoot (getCode(file));
    }
  }

  def getCode(clazz: File): TreeItem[TreeNodes] = {
    val jclazz = new ClassParser(clazz getAbsolutePath);
    val parsed = jclazz parse
    val methods = parsed getMethods

    val data = Nil;
    val root = new TreeItem[TreeNodes](new ClassNode(parsed));

    sourceCodeLookup clear;

    root.getChildren addAll (JavaConversions.asJavaCollection(
      methods.map(meth => {
        val methNode = new TreeItem[TreeNodes](new MethodNode(meth));
        methNode.getChildren addAll (JavaConversions.asJavaCollection(process(meth)))
        methNode
      })));

    root
  }

  def process(meth: Method): Array[TreeItem[TreeNodes]] = {
    val code = meth getCode;
    val instructions = getInstructionsWithOffsets(code);

    return instructions.map((a) => {
      val lineNum = meth.getLineNumberTable() getSourceLine (a._1);
      val data = new LineAndCode(lineNum, InstructionConverter.convert(a._2), meth);
      val node = new TreeItem[TreeNodes](data);
      sourceCodeLookup(data) = lineNum
      node
    });
  }

  def getInstructionsWithOffsets(code: Code): Array[(Int, org.apache.bcel.generic.Instruction)] = {
    val bytes = new ByteSequence(code getCode);

    var lookup: Array[(Int, org.apache.bcel.generic.Instruction)] = Array.empty;

    while (bytes.available > 0) {
      val x = (bytes getIndex, org.apache.bcel.generic.Instruction.readInstruction(bytes));
      lookup = lookup :+ x;
    }
    lookup
  }
}