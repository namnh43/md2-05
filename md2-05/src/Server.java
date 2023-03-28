/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package caro.carov2;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Kid
 */
public class Server {
    JButton[][] bt;
    static boolean flat = false;//Chua click
    public static JFrame f;
    JPanel p;
    JTextArea textout;
    JTextArea textin;
    JButton send, newgame;
    JScrollPane jscroll1, jscroll2;
    JLabel label1, label2, label3;
    public int i, j;
    Socket socket;
    ServerSocket serversocket;
    OutputStream os;
    InputStream is;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    int xx, yy, x, y;
    int[][] matran;
    int[][] matrandanh;
    private final MenuBar menubar;

    public Server() {
        f = new JFrame();
        f.setTitle("Server- thachsung");
        f.setSize(700, 500);
        x = 10;
        y = 10;

        matran = new int[x][y];
        matrandanh = new int[x][y];
        menubar = new MenuBar();
        bt = new JButton[x][y];
        p = new JPanel();
        jscroll1 = new JScrollPane();
        jscroll2 = new JScrollPane();
        textout = new JTextArea();
        textin = new JTextArea();
        send = new JButton("GỬI");
        label1 = new JLabel("PHẦN MỀM CHƠI GAME");
        label2 = new JLabel("CỜ CARO VÀ CHAT");
        label3 = new JLabel("Nhóm:CHICKEN_ET02");
        label1.setBounds(430, 40, 300, 50);
        label2.setBounds(450, 65, 300, 50);
        label3.setBounds(430, 90, 300, 50);
        label1.setFont(new Font("TimesRoman", Font.BOLD, 20));
        label1.setForeground(Color.red);
        label2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        label2.setForeground(Color.red);
        label3.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        label3.setForeground(Color.GREEN);
        f.add(label1);
        f.add(label2);
        f.add(label3);
        ///
        //f.add(textout);
        f.setMenuBar(menubar);
        Menu game = new Menu("Game");
        menubar.add(game);
        Menu help = new Menu("Help");
        menubar.add(help);
        MenuItem helpItem = new MenuItem("Help");
        help.add(helpItem);
        MenuItem About = new MenuItem("About..");
        help.add(About);
        MenuItem newItem = new MenuItem("New Game");
        game.add(newItem);
        MenuItem exit = new MenuItem("Exit");
        game.add(exit);
        game.addSeparator();
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newgame();
                try {
                    oos.writeObject("newgame,123");
                } catch (IOException ex) {
                    //
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        //
        f.add(send);
        f.getContentPane().add(jscroll2);
        jscroll2.setViewportView(textout);
        //jscroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jscroll2.setBounds(420, 200, 265, 150);
        textout.setLineWrap(true);
        //textout.setWrapStyleWord(true);
        textout.setEnabled(false);
        f.getContentPane().add(jscroll1);
        jscroll1.setViewportView(textin);
        jscroll1.setBounds(420, 360, 200, 50);
        textin.setLineWrap(true);
        ///
        p.setBounds(10, 30, 400, 400);
        send.setBounds(625, 360, 60, 49);
        send.setBackground(Color.WHITE);
        p.setLayout(new GridLayout(x, y));
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(p);
        f.getContentPane().setBackground(Color.BLACK);
        f.setVisible(true);
        f.setResizable(false);
        //game caro
        for (i = 0; i < x; i++) {
            for (j = 0; j < y; j++) {
                final int a = i, b = j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.ORANGE);
                bt[a][b].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        flat = true;//Ben Server da click
                        matrandanh[a][b] = 1;
                        bt[a][b].setEnabled(false);
                        if (Client.flat)//Neu ben Client da click
                            setEnableButton(true);//Ban duoc phep click
                        else setEnableButton(false); //Ban khong duoc phep click
                        bt[a][b].setIcon(new ImageIcon("E:\\STUDY\\Java\\cocaro\\src\\caro\\o1.png"));
                        //bt[a][b].setBackground(Color.BLUE);
                        try {
                            oos.writeObject("caro," + a + "," + b);//Tra chuoi toa do ve Client
                        } catch (Exception ex) {
                            //
                        }
                    }
                });
                p.add(bt[a][b]);
            }
        }

        ////
        //chat
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textin.getText().equals("") == false) {
                        oos.writeObject("chat," + textin.getText());
                        textout.append("Toi:" + textin.getText() + '\n');
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Loi1");
                }

                textin.setText("");
            }
        });
        ///

        initMatran();
        try {
            serversocket = new ServerSocket(1234);
            System.out.println("Dang doi client...");
            socket = serversocket.accept();
            System.out.println("Client da ket noi");
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            while (true) {
                String stream = ois.readObject().toString();
                String[] data = stream.split(",");
                if (data[0].equals("chat")) {
                    textout.append("Khach:" + data[1] + '\n');
                } else if (data[0].equals("caro")) {
                    caro(data[1], data[2]);
                    setEnableButton(true);
                } else if (data[0].equals("newgame")) {
                    newgame();
                } else if (data[0].equals("dontplay")) {
                    setEnableButton(false);
                }
            }
        } catch (Exception ee) {
        }
    }

    public void caro(String x, String y) {
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);

        //Danh dau vi tri doi thu danh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        bt[xx][yy].setIcon(new ImageIcon("E:\\STUDY\\Java\\cocaro\\src\\caro\\x1.png"));

        //Kiem tra xem da thang cuoc hay chua?
        System.out.println("CheckH:" + checkHang());
        System.out.println("CheckC:" + checkCot());
        System.out.println("CheckCp:" + checkCheoPhai());
        System.out.println("CheckCt:" + checkCheoTrai());
        if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1) {

            Object[] options = {"Dong y", "Huy bo"};
            int m = JOptionPane.showConfirmDialog(f, "Ban da thua.Ban co muon choi lai khong?", "Thong bao", JOptionPane.YES_NO_OPTION);
            if (m == JOptionPane.YES_OPTION) {
                setVisiblePanel(p);
                newgame();
                try {
                    oos.writeObject("newgame,123");
                } catch (IOException ie) {
                    //
                }
            } else if (m == JOptionPane.NO_OPTION) {
                setEnableButton(false);
                try {
                    oos.writeObject("dontplay,123");
                } catch (IOException ie) {
                    //
                }
            }
        }
    }

    public void newgame() {
        for (i = 0; i < x; i++)
            for (j = 0; j < y; j++) {
                bt[i][j].setIcon(null);
                matran[i][j] = 0;
                matrandanh[i][j] = 0;
            }
        setEnableButton(true);
    }

    public void setEnableButton(boolean b) {
        for (i = 0; i < x; i++)
            for (j = 0; j < y; j++) {
                if (matrandanh[i][j] == 0)
                    bt[i][j].setEnabled(b);
            }
    }

    public void setVisiblePanel(JPanel pHienthi) {
        //	f.removeAll();
        f.add(pHienthi);
        pHienthi.setVisible(true);
        pHienthi.updateUI();
    }

    public void initMatran() {
        for (i = 0; i < x; i++)
            for (j = 0; j < y; j++) {
                matran[i][j] = 0;
            }
    }

    public void inMatran() {
        for (i = 0; i < x; i++) {
            for (j = 0; j < y; j++) {
                System.out.print(matran[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int checkHang() {
        int win = 0, hang = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        hang++;
                        if (hang > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        hang = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    hang++;
                } else {
                    check = false;
                }
            }
            hang = 0;
        }
        return win;
    }

    public int checkCot() {
        int win = 0, cot = 0;
        boolean check = false;
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        cot++;
                        if (cot > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    cot++;
                } else {
                    check = false;
                }
            }
            cot = 0;
        }
        return win;
    }

    public int checkCheoPhai() {
        int win = 0, cheop = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = x - 1; i >= 0; i--) {
            for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[n - j][j] == 1) {
                        cheop++;
                        //System.out.print("+"+j);
                        if (cheop > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheop = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheop++;
                } else {
                    check = false;
                }
            }
            cheop = 0;
            check = false;
        }
        return win;
    }

    public int checkCheoTrai() {
        int win = 0, cheot = 0, n = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = y - 1; j >= 0; j--) {
                if (check) {
                    if (matran[n - j - 2 * cheot][j] == 1) {
                        cheot++;
                        System.out.print("+" + j);
                        if (cheot > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheot++;
                } else {
                    check = false;
                }
            }
            n = 0;
            cheot = 0;
            check = false;
        }
        return win;
    }

    public int getwith() {
        return f.getWidth();
    }

    public int getheight() {
        return f.getHeight();
    }

    public static void main(String[] a) {
        new Server();
    }
}
