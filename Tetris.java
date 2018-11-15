import java.util.Scanner;
public class Tetris
{
    static int n = 10, m = 20,p,x,y,wide;
    static int tool1,tool2;
    static int score = 0,max=0;
    static int[][] map = new int[n + 14][m + 14];
    static  int[] currentblock;
    public static void main(String arg[])
    {
        String input;
        int decision;
        Scanner sc = new Scanner(System.in);
        initialize();
        while (true)
        {
            System.out.println("输入吧:");
            input=sc.nextLine();

            switch (input)
            {

                case  "C":
                    if (tool1>0)
                    {
                        tool1--;
                        clearline(m - 1);
                        print();
                    }
                    else
                        System.out.println("道具不足！");
                    break;
                case  "S":
                    if (tool2>0)
                    {
                        tool2--;
                        newblock();
                        print();
                    }
                    else
                        System.out.println("道具不足！");
                    break;
                case  "Q":
                    System.exit(0); break;
                case  "T":
                    System.out.println("当前最高分："+max); break;
                case  "R":
                    initialize(); break;
                case  "W":
                    Spin(); break;
                case "1":case "2":case "3":case "4":case "5":case "6":case "7":case "8":case "9":case "10":
                    x=Integer.valueOf(input)-1-findblank(0,currentblock);
                    y=0-findblank(1,currentblock);
                    decision= checkplace(x,y,currentblock);
                    if (decision==0)
                    {
                        System.out.println("那个位置不可以！ (,,• . •̀,)");
                        break;
                    };
                    if ((decision==-1)||(checkplace(x,y+1,currentblock)==-1))
                    {
                        addfinal(x,y,currentblock);
                        endgame();
                        break;
                    }
                    drop(x,y,currentblock);
                    newblock();
                    print();
                    break;
                default:
                    System.out.println("请输入1~10的数字，或者字母“Q” “R” “W” “T” 哦：）");
            }
        }
    }

    public static void initialize()//一局游戏初始化
    {
        tool1=5;
        tool2=10;
        score = 0;
        for (int j = 0; j < m+6; j++)
        {
            for (int i = 0; i < n+6; i++)
            {
            if ((j<m)&&(i<n)) map[i][j] = 0;
            else map[i][j]=2;
            }
        }
        newblock();
        print();
    }

    public static void print()//打印游戏界面
    {
        System.out.println("SCORE:" + score);
        System.out.println("NEXT:");

            for (int j = 0; j < wide; j++)
            {
                for (int i = 0; i < wide-1; i++)
                {

                    if (currentblock[i + j * wide] == 0) System.out.print(' ');
                    else System.out.print('■');
                };

                if (currentblock[wide-1 + j * wide] == 0) System.out.println(' ');
                else System.out.println('■');

            }
        System.out.println("┌──────────┐");//打印桌面
        for (int j = 0; j < m; j++) {
            System.out.print("│");
            for (int i = 0; i < n; i++) {
                if (map[i][j] == 0 ) System.out.print(' ');
                else System.out.print('■');
            }
            System.out.println("│");
        }
        System.out.println("└──────────┘\n"+
                           " 12345678910\n");
        System.out.println(
                " C          使用消除道具(剩余:"+tool1+")  \n" +
                " S          使用随机道具(剩余:"+tool2+")  \n" +
                " 1~10数字    将当前方块的最左端对齐到相应位置并下落\n" +
                " W          旋转\n" +
                " R          重新开始\n" +
                " T          最高分\n"+
                " Q          退出");
    };

    public static void Spin()//旋转方块
    {
        int[] spinblock = {6, 3, 0,
                7, 4, 1,
                8, 5, 2};

        int[] spinlong = {12, 8,4, 0,
                13, 9,5, 1,
                14, 10,6, 2,
                15,11,7, 3};
        int pass[]=currentblock.clone();
        if (p==5)
        {
            print();
            return;
        }
        else if (p==6)
        {

            for (int i=0;i<16;i++)
                currentblock[i]=pass[spinlong[i]];
        }
        else
        {

            for (int i=0;i<9;i++)
                currentblock[i]=pass[spinblock[i]];
        }
        print();
    }

    public static void drop(int x,int y,int[] currentblock)//方块下落
    {
        int leftside=findblank(0,currentblock);
        int j;
        for( j=y;j<m;j++)
        {
            if (checkplace(x,j,currentblock)<=0) break;
        }
        j--;
        y=j;
        for ( j=y;j<y+wide;j++)
        {
            for (int i = x + leftside; i < x + wide; i++)
            {
                if (currentblock[(i - x) + (j - y) * wide] == 1) map[i][j] = 1;
            }

        }
        int line=findfull();
        while (line!=-1)
        {
            clearline(line);
            score+=10;
            line=findfull();

        }
    }

    public static int checkplace(int xx,int yy,int[] current)//检查方块能否放在某位置
    {
        int leftside=findblank(0,current),upside=findblank(1,current);
        boolean outside=false,able=true;
        for (int j=yy+upside;j<yy+wide;j++)
            for(int i=xx+leftside;i<xx+wide;i++)
            {
                if (current[(i-xx)+(j-yy)*wide]==1)
                {
                    if (map[i][j]==2) outside=true;
                    if (map[i][j]>=1) able=false;
                }
            }
        if (outside) return 0;
        if (able) return 1;
        return -1;


    }

    public static int findblank(int p,int[] currentblock)//p==0表示当前方块左侧空了几列；p==1表示当前方块上方空了几行
    {
        int i;
        for (i=0;i<wide;i++)
        {
            boolean flag=false;
            for (int j=0;j<wide;j++)
                if (((p==0)&&(currentblock[i+j*wide]==1))||((p==1)&&(currentblock[j+i*wide]==1)))
                {
                    flag=true;
                    break;
                }
            if (flag) break;
        }
        return i;
    }

    public static void newblock()//生成一个新方块
    {
        int[] longblock = {
                0, 0, 0, 0,
                1, 1, 1, 1,
                0, 0, 0, 0,
                0, 0, 0, 0};
        int[] square = {
                1, 1,
                1, 1};

        int[][] block =
                {
                        {1, 0, 0, 1, 1, 1, 0, 0, 0},
                        {0, 0, 1, 1, 1, 1, 0, 0, 0},
                        {0, 1, 1, 1, 1, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0},
                        {0, 1, 0, 1, 1, 1, 0, 0, 0}};

        x=4;y=0;
        p = (int) (Math.random() * 7);
        if (p==6)
        {
            currentblock=longblock.clone();
            wide=4;
        }
        else if (p==5)
        {
            currentblock=square.clone();
            wide=2;
        }
        else
        {
            currentblock=block[p].clone();
            wide=3;
        }
    }

    public static void clearline(int l)//消行
    {
        for (int j=l;j>0;j--)
        {
            for(int i=0;i<m;i++)
                map[i][j]=map[i][j-1];
        }
    }

    public static int findfull()//找出地图中有无满了可消去的行
    {

        for (int j=0;j<=m-1;j++)
        {
            boolean full=true;
            for (int i=0;i<n;i++)
            {
                if (map[i][j]==0)
                {
                    full=false;
                    break;
                }
            }
            if (full)  return j;
        }

        return -1;
    }

    public static void endgame()//结束
    {
        int[][] amap= new int[n + 14][m + 14];//找坑洞用的临时数组
        String input;
        Scanner sc = new Scanner(System.in);
        for (int i=0;i<n;i++)
            for (int j=0;j<m;j++)
                amap[i][j]=map[i][j];
        score-=sumhole(0,amap);
        for (int i=0;i<n;i++)
            for (int j=0;j<m;j++)
                amap[i][j]=map[i][j];
        score+=sumhole(1,amap);
        print();
        System.out.println("游戏结束");
        System.out.println("你的总分："+score);
        if (score==max)
            System.out.println("您平了最高纪录");
        else if (score>max)
        {
            System.out.println("恭喜您打破了最高纪录！");
            System.out.println("过去记录为："+max);
            System.out.println("当前记录为："+score);
            max=score;
        }
        System.out.println(" ");
        System.out.println("按R再来一局，按Q退出游戏");
        while (true)
        {
            input = sc.nextLine();
            if (input.equals("R") )
            {
                initialize();
                break;
            }
            if (input.equals("Q")) System.exit(0);
            else System.out.println("按R再来一局，按Q退出游戏");
        }

    }

    public static void addfinal(int x,int y,int[] currentblock)//把导致游戏结束的方块加入地图
    {
        int[] cblock=new int[wide*wide];
        for (int i=0;i<wide*wide;i++)
            cblock[i]=currentblock[i];
        int up=findblank(1,cblock);
        y=-findblank(1,cblock);
        while ((checkplace(x,y,cblock)!=1))
        {
            for (int i=0;i<wide;i++)
              cblock[i+wide*up]=0;
            y=-findblank(1,cblock);
            up=findblank(1,cblock);
        }
        for ( int j=y+up;j<y+wide;j++)
        {
            for (int i = x +findblank(0,cblock); i < x + wide; i++)
            {
                if (cblock[(i - x) + (j - y) * wide] == 1) map[i][j] = 1;
            }

        }



    }

    public static int sumhole(int k,int[][] amap)//计算孔洞数，k=0时计算孔洞总数，k=1时计算连接至顶端的孔洞数
    {
        int holes=0;
        if (k==0)
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                if (amap[i][j]==0)
                {
                    paint(i,j,amap);
                    holes++;
                }
            }
        }
        else
            for (int i=0;i<n;i++)
                if (amap[i][0]==0)
                {
                        paint(i,0,amap);
                        holes++;
                }

        return  (holes);
    }

    public static void paint(int x1,int y1,int[][]amap)//搜索染色，保证不会重复计算孔洞数
    {
        amap[x1][y1]=2;
        int xt[]={0,1,0,-1},yt[]={1,0,-1,0};
        for (int i=0;i<4;i++)
            if ((x1+xt[i]>=0) &&(x1+xt[i]<n)&&(y1+yt[i]>=0)&&(y1+yt[i]<m)&&(amap[x1+xt[i]][y1+yt[i]]==0)) paint(x1+xt[i],y1+yt[i],amap);

    }
}

