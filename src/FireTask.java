import java.util.concurrent.RecursiveTask;

public class FireTask extends RecursiveTask<FireMapParallel.StepResult>{

    private final int rowLo, rowHi, colLo, colHi;
    private final FireMapParallel map;
    static final int CUTOFF = 16;

    public FireTask(int inRowLow, int inRowHi, int inColLow, int inColHi, FireMapParallel inMap){
        this.rowLo = inRowLow;
        this.rowHi = inRowHi;
        this.colLo = inColLow;
        this.colHi = inColHi;
        this.map = inMap;
    }

    protected FireMapParallel.StepResult compute(){

        int rowSize = rowHi - rowLo;
        int columnSize = colHi - colLo;

        if (((rowSize) <= CUTOFF) && ((columnSize) <= CUTOFF)){ // if row length and column length is within cutoff
            FireMapParallel.StepResult result =  map.updateRegion(map.getMode(), rowLo, rowHi, colLo, colHi);
            return result;
        } else { // row and/or column too long
            if ((rowSize) >= (columnSize)){ // Row length is longer than column length
                int midpt = (rowLo + rowSize/2);
                FireTask left = new FireTask(rowLo, midpt, colLo, colHi, map);//create lower task
                FireTask right = new FireTask(midpt, rowHi, colLo, colHi, map);//create higher task
                left.fork();// move lower task to new thread
                FireMapParallel.StepResult rightResult = right.compute();//run higher task
                FireMapParallel.StepResult leftResult = left.join();//await lower task completion
                FireMapParallel.StepResult finalResult = FireMapParallel.StepResult.combine(rightResult, leftResult);
                return finalResult;

            } else { // Column length is longer than row length
                int midpt = (colLo + columnSize/2);
                FireTask left = new FireTask(rowLo, rowHi, colLo, midpt, map);
                FireTask right = new FireTask(rowLo, rowHi, midpt, colHi, map);
                left.fork();//move lower task to new thread
                FireMapParallel.StepResult rightResult = right.compute();//run higher task
                FireMapParallel.StepResult leftResult = left.join();//await lower task completion
                FireMapParallel.StepResult finalResult = FireMapParallel.StepResult.combine(rightResult, leftResult);
                return finalResult;
            }
        }
    }
    
}
