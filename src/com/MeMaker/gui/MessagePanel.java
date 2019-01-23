package com.MeMaker.gui;

import com.MeMaker.controller.MessageServer;
import com.MeMaker.model.Message;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class MessagePanel extends JPanel implements ProgressDialogListner{

    private JTree serverTree;
    private ServerTreeCellRenderer treeCellRenderer;
    private ServerTreeCellEditor treeCellEditor;
    private ProgressDialog progressDialog;

    private Set<Integer> selectedServers;
    private MessageServer messageServer;
    private SwingWorker<List<Message>, Integer> worker;

    private TextPanel textPanel;
    private JList<Message> messageList;
    private JSplitPane lowerPane;
    private DefaultListModel<Message> messageListModel;



    MessagePanel(JFrame parent) {

        messageListModel = new DefaultListModel<>();

        progressDialog = new ProgressDialog(parent,"Messages Downloading...");
        messageServer = new MessageServer();

        progressDialog.setProgressDialogListener(this);

        selectedServers = new TreeSet<>();
        selectedServers.add(0);
        selectedServers.add(1);
        selectedServers.add(5);



        treeCellRenderer = new ServerTreeCellRenderer();
        treeCellEditor = new ServerTreeCellEditor();

        serverTree = new JTree(createTree());
        serverTree.setCellRenderer(treeCellRenderer);
        serverTree.setCellEditor(treeCellEditor);
        serverTree.setEditable(true);


        serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // sets selection model so you can select one node at once, multiple selection not allowed in this mode

        messageServer.setSelectedServers(selectedServers);

        treeCellEditor.addCellEditorListener(new CellEditorListener() {
           @Override
           public void editingStopped(ChangeEvent e) {
               ServerInfo info = (ServerInfo) treeCellEditor.getCellEditorValue();

               int serverID = info.getId();

               if(info.isChecked()) {
                   selectedServers.add(serverID);
               } else {
                   selectedServers.remove(serverID);
               }

               messageServer.setSelectedServers(selectedServers);

               retrieveMessages();

           }

           @Override
           public void editingCanceled(ChangeEvent e) {
           }
       });

        setLayout(new BorderLayout());

        textPanel = new TextPanel();
        messageList = new JList<>(messageListModel);
        messageList.setCellRenderer(new MessageListRenderer());

        messageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Message message = messageList.getSelectedValue();

               textPanel.setText(message.getContent());
            }
        });

        serverTree.setMinimumSize(new Dimension(10,1000));
        lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(messageList),textPanel);
        JSplitPane upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(serverTree), lowerPane);


        textPanel.setMinimumSize(new Dimension(10,100));
        messageList.setMinimumSize(new Dimension(10,100));

        upperPane.setResizeWeight(0.5);
        lowerPane.setResizeWeight(0.5);

        add(upperPane, BorderLayout.CENTER);



    }

    void refresh() {
        retrieveMessages();
    }

    private void retrieveMessages() {

        progressDialog.setVisible(true);
        progressDialog.setMaximum(messageServer.getMessageCount()); // sets maximum number for progress bar
        worker = new SwingWorker<>() {

            // In this method you write code which is run in new thread
            @Override
            protected List<Message> doInBackground() {

                List<Message> retrievedMessages = new ArrayList<>();

                int count = 0;

                for (Message message : messageServer) {

                    if (isCancelled()) break; // closes window after exiting/canceling window

                    System.out.println(message.getTitle());
                    retrievedMessages.add(message);

                    count++;

                    publish(count);
                }
                return retrievedMessages;
            }

            @Override
            protected void process(List<Integer> counts) {
                int retrieved = counts.get(counts.size() - 1);
                progressDialog.setValue(retrieved);

            }

            // done is called when thread is finished
            @Override
            protected void done() {

                progressDialog.setVisible(false);

                if (isCancelled()) return;

                try {
                    List<Message> retrievedMessages = get();

                    messageListModel.removeAllElements();

                    for (Message message : retrievedMessages) {
                        messageListModel.addElement(message);
                    }

                    messageList.setSelectedIndex(0);

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
        };

        worker.execute();

        }


    private DefaultMutableTreeNode createTree(){
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

        DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
        DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York",0,selectedServers.contains(0)));
        DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston",1,selectedServers.contains(1)));
        DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Los Angeles",2,selectedServers.contains(2)));


        branch1.add(server1);
        branch1.add(server2);
        branch1.add(server3);


        DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
        DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London",3,selectedServers.contains(3)));
        DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Manchester",4,selectedServers.contains(4)));
        DefaultMutableTreeNode server6 = new DefaultMutableTreeNode(new ServerInfo("Oxford",5,selectedServers.contains(5)));

        branch2.add(server4);
        branch2.add(server5);
        branch2.add(server6);

        top.add(branch1);
        top.add(branch2);


        return top;
    }


    @Override
    public void progressDialogCanceled() {
        System.out.println("Cancelled");

        if(worker != null) {
            worker.cancel(true);
        }
    }
}
