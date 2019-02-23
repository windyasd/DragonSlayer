package huawei;

import huawei.exam.*;
import org.junit.Test;


/**
 * 实现类
 * 
 * 各方法请按要求返回，考试框架负责报文输出
 */
public class DragonSlayerImpl implements ExamOp
{
    /**
     * ReturnCode(返回码枚举) .S001：重置成功 .S002：设置火焰成功 .S003：设置龙卷风成功 .S004：设置传送阵成功 .E001：非法命令
     * .E002：非法坐标 .E003：非法时间 .E004：操作时间不能小于系统时间 .E005：该区域不能设置元素 .E006：龙卷风数量已达上限
     * .E007：传送阵数量已达上限 .E008：传送阵的入口和出口重叠
     */
    
    /**
     * Area(区域类) int getX()：获取横坐标 void setX(int x)：设置横坐标 int getY()：获取纵坐标 void setY(int
     * y)：设置纵坐标 Element getElement()：获取元素 void setElement(Element element)：设置元素 boolean
     * equals(Object o)：区域横纵坐标相同，则区域相同
     */
    
    /**
     * Element(元素枚举) .NONE：空元素 .HERO：英雄 .DRAGON：恶龙 .FIRE：火焰 .TORNADO：龙卷风 .PORTAL：传送阵
     */
    
    /**
     * Hero(英雄类) Title getTitle()：获取称号 void setTitle(Title title)：设置称号 Status
     * getStatus()：获取状态 void setStatus(Status status)：设置状态 Area getArea()：获取区域
     * setArea(Area area)：设置区域
     */
    
    /**
     * Title(称号枚举) .WARRIOR：勇士 .DRAGON_SLAYER：屠龙者
     */
    
    /**
     * Status(状态枚举) .MARCHING：行进 .WAITING：等待
     */

    
    /**
     * 待考生实现，构造函数
     */
     int[][] Game_Ground=new int[16][16];
     int TickTime=0;
     int HasTornado=0;
     int HasPortal=0;
    int[][] adjMatrix=AdjMatrix_Ini(Game_Ground);
     Hero hero=new Hero(Title.WARRIOR,Status.WAITING,new Area(0,0));
     int[][] route;
     int stepRemain;
    public DragonSlayerImpl()
    {
        //初始化地下城
        /*
        //NONE(0, ""),
        //HERO(1, "英雄"),
        //DRAGON(2, "恶龙"),
       //FIRE(3, "火焰"),
        //TORNADO(4, "龙卷风"),
       // PORTAL(5, "传送阵");
        //本程序认为6为传送阵出口
    //*/

    }

    /**
     * 检测方法
     */
    @Test
    public void testRoute_Find()
    {
        OpResult K=reset();
//        int[][] route=Route_Find(hero,adjMatrix);
        Status_Time(5,route);
        System.out.printf("x:%d,y:%d\n",hero.getArea().getX(),hero.getArea().getY());
        for(int i:route[0])
        {
            System.out.println(i);
        }
    }

    /**
     * 指定时刻运行状态函数
     */
    public void Status_Time(int time,int[][] route1)
    {
        if (hero.getStatus()==Status.MARCHING)
        {
            int RunTime=time-TickTime;
            int i=0;
            while(RunTime>=route1[1][i]&&route1[1][i]!=666)
            {
                RunTime=RunTime-route1[1][i];
                i++;
            }
            if (route1[0][i]==255)
            {
                stepRemain=0;
                hero.setStatus(Status.WAITING);
                hero.setTitle(Title.DRAGON_SLAYER);
                hero.setArea(new Area(route1[0][i]%16,route1[0][i]/16));
            }else {
                stepRemain=RunTime;
                hero.setArea(new Area(route1[0][i]%16,route1[0][i]/16));
            }
        }
    }

    /**
     * 最短路径寻找算法
     */
    public int[][] Route_Find(Hero hero,int[][] adjMatrix)
    {
        int s=hero.getArea().getX()+hero.getArea().getY()*16;//起点
        int i,j,n=256;
        int[] pre=new int[256];
        int[] dist=adjMatrix[s].clone();
        //如果前一状态为等待状态，将英雄在龙卷风中走过的距离置0
        if (hero.getStatus()==Status.WAITING)
        {
            stepRemain=0;
        }
        boolean[] S=new boolean[n];
        boolean[] Flag=new boolean[n];
        for (i=0;i<n;i++)
        {
            S[i]=false;
            Flag[i]=false;
            if (i!=s&&dist[i]<1000)
            {
                pre[i]=s;
            }else
            {
                pre[i]=-1;
            }
        }

        S[s]=true;
        int Min;
        int v;
        for (i=0;i<n;i++)
        {
            Min=10000;
            v=s;
            for (j=0;j<n;j++)
            {
                if(S[j]==false&&dist[j]<Min)
                {
                    v=j;
                    Min=dist[j];
                }
            }
            S[v]=true;
            for (j=0;j<n;j++)
            {
                if (S[j]==false&&adjMatrix[v][j]<10000&&dist[v]+adjMatrix[v][j]==dist[j]&&v!=j)
                {
                    Flag[j]=true;//如果存在两条相同的路线，置为TRUE
                }
                else if (S[j]==false&&adjMatrix[v][j]<10000&&dist[v]+adjMatrix[v][j]<dist[j])
                {
                    dist[j]=dist[v]+adjMatrix[v][j];
                    pre[j]=v;
                    Flag[j]=false;
                }
            }
        }
        int[][] route1=new int[2][200];
        route1[0][0]=255;
        route1[1][0]=666;
        int k,num;
        boolean flag=false;
        k=255;
        if (pre[k]==-1)
        {
            flag=true;
            num=0;
        }else {
            num=1;
            while(pre[k]!=s)
            {
                int x=pre[k]%16;
                int y=pre[k]/16;
                switch (Game_Ground[x][y])
                {
                    case 0:    //没有元素
                        route1[1][num]=1;
                        break;
                    case 4:     //龙卷风
                        route1[1][num]=3;
                        break;
                    case 5:     //传送门入口
                        route1[1][num]=0;
                        break;
                    case 6:     //传送门出口
                        route1[1][num]=1;
                        break;
                    default:
                        break;

                }
                route1[0][num++]=pre[k];
                k=pre[k];
            }
            route1[0][num]=s;
            switch (Game_Ground[s%16][s/16])
            {
                case 0:    //没有元素
                    route1[1][num]=1;
                    break;
                case 1:
                    route1[1][num]=1;
                    break;
                case 4:     //龙卷风
                    route1[1][num]=3;
                    break;
                case 5:     //传送门入口
                    route1[1][num]=0;
                    break;
                case 6:     //传送门出口
                    route1[1][num]=1;
                    break;
                default:
                    break;
            }
            route1[1][num]=route1[1][num]-stepRemain;
            for(i=1;i<num;i++)
            {
                if (Flag[route1[0][i]]){
                    flag=true;
                }
            }
        }

        if (flag)
        {
            hero.setStatus(Status.WAITING);
        }else {
            hero.setStatus(Status.MARCHING);
        }

        int [][] route_2=new int[2][200];
        for (int p=num;p>=0;p--)
        {
            route_2[0][num-p]=route1[0][p];
            route_2[1][num-p]=route1[1][p];
        }
        return route_2;
    }

    /**
     * 邻接矩阵初始化函数
     */
    public int[][] AdjMatrix_Ini(int[][] Game_Ground)
    {
        int[][] adjMatrix=new int[256][256];
        for(int a=0;a<256;a++)
        {
            for(int b=0;b<256;b++)
            {
                if (a==b)
                {
                    adjMatrix[a][b]=0;
                }
                else
                {
                    adjMatrix[a][b]=10000;
                }
            }

        }
        int point=0;
        for(int j=0;j<16;j++)
        {
            for(int i=0;i<16;i++)
            {
                point=i+j*16;
                //左下点
                if(i-1>=0&&j-1>=0)
                {
                    int x,y;
                    x=point;
                    y=i-1+(j-1)*16;
                    adjMatrix[x][y]=1;
                }
                //正下点
                if(j-1>=0)
                {
                    int x,y;
                    x=point;
                    y=i+(j-1)*16;
                    adjMatrix[x][y]=1;
                }
                //右下点
                if(i+1<16&&j-1>=0)
                {
                    int x,y;
                    x=point;
                    y=i+1+(j-1)*16;
                    adjMatrix[x][y]=1;
                }
                //左边
                if(i-1>=0)
                {
                    int x,y;
                    x=point;
                    y=i-1+j*16;
                    adjMatrix[x][y]=1;
                }
                //右边
                if(i+1<16)
                {
                    int x,y;
                    x=point;
                    y=i+1+j*16;
                    adjMatrix[x][y]=1;
                }
                //左上点
                if(i-1>=0&&j+1<16)
                {
                    int x,y;
                    x=point;
                    y=i-1+(j+1)*16;
                    adjMatrix[x][y]=1;
                }
                //正上点
                if(j+1<16)
                {
                    int x,y;
                    x=point;
                    y=i+(j+1)*16;
                    adjMatrix[x][y]=1;
                }
                //右上点
                if(i+1<16&&j+1<16)
                {
                    int x,y;
                    x=point;
                    y=i+1+(j+1)*16;
                    adjMatrix[x][y]=1;
                }
            }

        }
        return adjMatrix;
    }

    /**
     * 邻接矩阵更改函数
     */
    public void AdjMatrix_Ref(int[][] adjMatrix,int i,int j,int distanse)
    {
        int point=i+j*16;
        //左下点
        if(i-1>=0&&j-1>=0)
        {
            int x,y;
            x=point;
            y=i-1+(j-1)*16;
            adjMatrix[x][y]=distanse;
        }
        //正下点
        if(j-1>=0)
        {
            int x,y;
            x=point;
            y=i+(j-1)*16;
            adjMatrix[x][y]=distanse;
        }
        //右下点
        if(i+1<16&&j-1>=0)
        {
            int x,y;
            x=point;
            y=i+1+(j-1)*16;
            adjMatrix[x][y]=distanse;
        }
        //左边
        if(i-1>=0)
        {
            int x,y;
            x=point;
            y=i-1+j*16;
            adjMatrix[x][y]=distanse;
        }
        //右边
        if(i+1<16)
        {
            int x,y;
            x=point;
            y=i+1+j*16;
            adjMatrix[x][y]=distanse;
        }
        //左上点
        if(i-1>=0&&j+1<16)
        {
            int x,y;
            x=point;
            y=i-1+(j+1)*16;
            adjMatrix[x][y]=distanse;
        }
        //正上点
        if(j+1<16)
        {
            int x,y;
            x=point;
            y=i+(j+1)*16;
            adjMatrix[x][y]=distanse;
        }
        //右上点
        if(i+1<16&&j+1<16)
        {
            int x,y;
            x=point;
            y=i+1+(j+1)*16;
            adjMatrix[x][y]=distanse;
        }
    }


    /**
     * 待考生实现，系统重置
     * 地下城元素重置
     * 英雄重置
     * 时间重置
     * @return 返回码
     */
    @Override
    public OpResult reset()
    {
        //时间重置
        TickTime=0;
        //地下城元素重置
        for (int i=0;i<16;i++)
        {
            for (int j=0;j<16;j++)
            {
                Game_Ground[i][j]=0;
            }
        }
        Game_Ground[0][0]=1;
        Game_Ground[15][15]=2;
        //英雄重置
        hero.setArea(new Area(0,0));
        hero.setStatus(Status.WAITING);
        hero.setTitle(Title.WARRIOR);
        //邻接矩阵重置
        adjMatrix=AdjMatrix_Ini(Game_Ground);
        //初始化路线
        route=Route_Find(hero,adjMatrix);
        //返回重置成功指令
        return new OpResult(ReturnCode.S001);
    }
    
    /**
     * 待考生实现，设置火焰
     * 
     * @param area 设置区域
     * @param time 设置时间
     * @return 返回码
     */
    @Override
    public OpResult setFire(Area area, int time)
    {
        if (time<TickTime)
        {
            return new OpResult(ReturnCode.E004);
        }
        else
            {
                Status_Time(time,route);
                TickTime=time;
                //后续路线更改
                if (area.getX()==hero.getArea().getX()&&area.getY()==hero.getArea().getY())
                {
                    return new OpResult(ReturnCode.E005);
                }
                if (Game_Ground[area.getX()][area.getY()]==0||Game_Ground[area.getX()][area.getY()]==6)
                {
                    Game_Ground[area.getX()][area.getY()]=3;
                    //更新邻接矩阵，并计算最短路径
                    AdjMatrix_Ref(adjMatrix,area.getX(),area.getY(),10000);
                    if (hero.getTitle()==Title.WARRIOR)
                    {
                        route=Route_Find(hero,adjMatrix);//英雄位置改变后路线重新计算，要优化时间
                    }
                    return new OpResult(ReturnCode.S002);
                }
                else {
                    return new OpResult(ReturnCode.E005);
                }
            }
    }
    
    /**
     * 待考生实现，设置龙卷风
     * 
     * @param area 设置区域
     * @param time 设置时间
     * @return 返回码
     */
    @Override
    public OpResult setTornado(Area area, int time)
    {
        if (time<TickTime)
        {
            return new OpResult(ReturnCode.E004);
        }
        else
        {

            //缺失计算程序
            Status_Time(time,route);
            TickTime=time;
            if (area.getX()==hero.getArea().getX()&&area.getY()==hero.getArea().getY())
            {
                return new OpResult(ReturnCode.E005);
            }
            if (HasTornado==0){
                if (Game_Ground[area.getX()][area.getY()]==0||Game_Ground[area.getX()][area.getY()]==6)
                {
                    Game_Ground[area.getX()][area.getY()]=4;
                    //更新邻接矩阵，并计算最短路径
                    AdjMatrix_Ref(adjMatrix,area.getX(),area.getY(),3);
                    if (hero.getTitle()==Title.WARRIOR)
                    {
                        route=Route_Find(hero,adjMatrix);//英雄位置改变后路线重新计算，要优化时间
                    }
                    return new OpResult(ReturnCode.S003);
                }
                else {
                    return new OpResult(ReturnCode.E005);
                }
            }else{
                return new OpResult(ReturnCode.E006);
            }
        }
    }
    
    /**
     * 待考生实现，设置传送阵
     * 
     * @param entry 入口区域
     * @param exit 出口区域
     * @param time 设置时间
     * @return 返回码
     */
    @Override
    public OpResult setPortal(Area entry, Area exit, int time)
    {
        if (time<TickTime)
        {
            return new OpResult(ReturnCode.E004);
        }
        else
        {

            //缺失计算程序
            Status_Time(time,route);
            TickTime=time;
            if (entry.getX()==hero.getArea().getX()&&entry.getY()==hero.getArea().getY())
            {
                return new OpResult(ReturnCode.E005);
            }
            if (entry.getX()==exit.getX()&&entry.getY()==exit.getY()){
                return new OpResult(ReturnCode.E008);
            }
            else{
                if (Game_Ground[entry.getX()][entry.getY()]==0)
                {
                    if (HasPortal==0)
                    {
                        Game_Ground[entry.getX()][entry.getY()]=5;
                        Game_Ground[exit.getX()][exit.getY()]=6;
                        //更新邻接矩阵，并计算最短路径
                        int point1=entry.getX()+entry.getY()*16;
                        int point2=exit.getX()+exit.getY()*16;
                        adjMatrix[point1][point2]=0;
                        if (hero.getTitle()==Title.WARRIOR)
                        {
                            route=Route_Find(hero,adjMatrix);//英雄位置改变后路线重新计算，要优化时间
                        }
                        return new OpResult(ReturnCode.S004);
                    }else{
                        return new OpResult(ReturnCode.E007);
                    }
                }
                else {
                    return new OpResult(ReturnCode.E005);
                }
            }

        }
    }
    
    /**
     * 待考生实现，查询
     * 
     * @param time 查询时间
     * @return 英雄信息
     */
    @Override
    public OpResult query(int time)
    {
        if (time<TickTime)
        {
            return new OpResult(ReturnCode.E004);
        }
        else {
            //缺失计算程序
            Status_Time(time,route);
            TickTime=time;
            if (hero.getTitle()==Title.WARRIOR)
            {
                route=Route_Find(hero,adjMatrix);//英雄位置改变后路线重新计算，要优化时间
            }
            //返回英雄状态
            OpResult opResult=new OpResult(hero);
            return opResult;
        }
    }
}