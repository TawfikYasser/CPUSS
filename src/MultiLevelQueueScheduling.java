import java.util.ArrayList;

public class MultiLevelQueueScheduling {
    public MultiLevelQueueScheduling() {

    }

    public void MLQ(int number,process[] processes,int quantum){
        float totalwt = 0, totaltat = 0;

        int[] completionTime = new int[number], waitingTime = new int[number], turnaroundTime = new int[number];


        ArrayList<Integer> RRQueue = new ArrayList<Integer>();
        ArrayList<Integer> FCFSQueue = new ArrayList<Integer>();

        int z = 0;

        for (int i = 0; i < number; i++) {
            if (processes[i].getQueueNumber() == 1) {
                RRQueue.add(i);

            }else{
                FCFSQueue.add(processes[i].getQueueNumber());
            }
        }

        int[] highPriorityProcessArray = new int[number];
        for (int i = 0; i < RRQueue.size(); i++) {
            highPriorityProcessArray[i] = RRQueue.get(i);
        }

        int rem_bt[] = new int[RRQueue.size()];
        for (int i = 0; i < RRQueue.size(); i++) {
            rem_bt[i] = processes[highPriorityProcessArray[i]].getBurset_time();
        }
        int rem_bt_2[] = new int[FCFSQueue.size()];
        for (int i =0;i<FCFSQueue.size();i++){
            rem_bt_2[i] = processes[FCFSQueue.get(i)].getBurset_time();
        }


        int t = completionTime[z]; // Current time in RR

        // Keep traversing processes in round robin manner until all of them are not done.


        int flag  =0;

        while (true) {
            boolean done = true;
            // Traverse all processes one by one repeatedly
            for (int i = 0; i < RRQueue.size(); i++)//0,1,3
            {

                if (processes[RRQueue.get(i)].getArrive_time() <= t && rem_bt[i] > 0) {


                    // If burst time of a process is greater than 0 then only need to process further
                    if (rem_bt[i] > 0) {
                        done = false;                                           // There is a pending process

                        if (rem_bt[i] > quantum) {
                            // Increase the value of t i.e. shows how much time a process has been processed
                            t += quantum;

                            // Decrease the burst_time of current process by quantum
                            rem_bt[i] -= quantum;
                        }

                        // If burst time is smaller than or equal to quantum. Last cycle for this process
                        else {
                            // Increase the value of t i.e. shows how much time a process has been processed
                            t = t + rem_bt[i];
                            completionTime[highPriorityProcessArray[i]] = t;

                            turnaroundTime[highPriorityProcessArray[i]] = completionTime[highPriorityProcessArray[i]]
                                    - processes[highPriorityProcessArray[i]].getArrive_time();
                            waitingTime[highPriorityProcessArray[i]] = turnaroundTime[highPriorityProcessArray[i]]
                                    - processes[highPriorityProcessArray[i]].getBurset_time();


                            // As the process gets fully executed make its remaining burst time = 0
                            rem_bt[i] = 0;
                        }
                    }

                }

                flag=0;
                for(int k = 0 ; k <RRQueue.size();k++){

                    if(rem_bt[k] == 0 || processes[RRQueue.get(k)].getArrive_time() > t){
                        flag++;
                    }
                }

            }


            int position =0;

            if(flag==RRQueue.size()){

                for (int j = 0; j < FCFSQueue.size(); j++) {//p3

                    String fl = " ";

                    do{
                        for(int y =0;y<number;y++){
                            if(processes[FCFSQueue.get(j)].getName().equals(processes[y].getName())){
                                position = y;
                                break;
                            }
                        }
                        completionTime[position] = t;
                        turnaroundTime[position] = completionTime[position] - processes[position].getArrive_time();
                        waitingTime[position] = turnaroundTime[position] - processes[position].getBurset_time();
                        t++;
                        rem_bt_2[j] -= 1;
                        for(int h = 0 ; h<RRQueue.size();h++){
                            if(t == processes[RRQueue.get(h)].getArrive_time()){
                                fl = "out";
                                break;
                            }
                        }

                    }while(fl.equals(" ") && rem_bt_2[j] >0);

                    if(!fl.equals(" ")){
                        break;
                    }
                }

            }




            // If all processes are done
            if (done == true)
                break;
        }


        System.out.println("\nPROCESS\t\t QUEUE NUMBER \tBURST TIME \tCOMPLETION TIME \tWAITING TIME \tTURNAROUNDTIME");
        for (int i = 0; i < number; i++) {
            System.out.println("\n" + processes[i].getName() + "\t\t\t\t" + processes[i].getQueueNumber() + "\t\t\t\t" + processes[i].getBurset_time() + "\t\t\t\t" + completionTime[i] + "\t\t\t\t" + waitingTime[i] + "\t\t\t\t" + turnaroundTime[i]);
        }

        for (int i = 0; i < number; i++) {
            totalwt += waitingTime[i];
            totaltat += turnaroundTime[i];
        }

        System.out.println("\n" + "Average Waiting Time is: " + totalwt / number);
        System.out.println("Average Turnaround Time is : " + totaltat / number);


    }
}
