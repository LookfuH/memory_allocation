public class Process implements Runnable {
    public String name;
    public int size;
    public int lifeTime;
    public GenericFit fit;

    public Process(String name, int size, int lifeTime, GenericFit fit) {
        this.name = name;
        this.size = size;
        this.lifeTime = lifeTime;
        this.fit = fit;
    }

    public void run() {
        try {
            Thread.sleep(lifeTime);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fit.remove(name);
    }

}

//                                       /##/*
//                                   ##(########%.
//                                 ,###%%%%%%%%#%%/
//                                 ,##.   ,.  .*#%/
//                                 %#/..@@,.$@*,(#%
//                               ###**,,/////(**,*/%%(
//                               #*,......,,..,,***/#*
//                                /,,*,.......,**(*//
//                                *#(///,,,,,*/((###                    ,,,(
//         ,,**                   ,#%%%%%#########%%%                 ,.*(
//           *,../              (#(//,,*/////////(######          /((,.,*,,**,..,(
//   (**,,.,..,.../####*     ((////(/,,....,,,***//#(///((#*  ##(##%%*,,.,,*,.,*#/
//     /,,,,..,*((.$%%####%#(//(##(*,..........,,,**/#(((((######%%%
//                  %$%%%##%(###%(*,,,.......,,,,,**//%%%######%%%
//                    ,$%%%%%%$$%(/**,,,,,,,,,*****//(%%#.%%%%$%
//                       ,,,    $%((//*****///////((##%%
//                              %%$%#((((((((((#####%%%%/
//                             /####%$%%%%%%%%%%%%%%%%###
//                             *%#####%%%.     $%%%%%#%%
//                               $%%%%%%%       $%%%%%%.
//                                 $$%%%%%     %%%$$$#
//                              /##(((#((##   ##(((((###*
//                           ((#(##(#/  *      */ ,(#(#((((
