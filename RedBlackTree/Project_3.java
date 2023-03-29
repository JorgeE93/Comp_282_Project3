package RedBlackTree;

import java.util.Scanner;

public class Project_3 {

    private final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // create the binary tree
        RBTree tree = new RBTree<>();
        boolean keepGoing = true;
        int userIn = -1;

        while(keepGoing){
            System.out.println("What would you like to do?");
            System.out.println("1. Add number");
            System.out.println("2. Delete number");
            System.out.println("3. Traverse Tree");
            System.out.println("4. Print Tree");
            System.out.println("0. Quit");
            userIn = getUserInput();
            switch (userIn){
                case 1:
                    System.out.println("Enter a Number to add: ");
                    userIn = getUserInput();
                    tree.insert(userIn);
                    break;
                case 2:
                    System.out.println("Enter a Number to delete: ");
                    userIn = getUserInput();
                    tree.delete(userIn);
                    break;
                case 3:
                    System.out.println("Which order of Traversal?");
                    System.out.println("1. Inorder");
                    System.out.println("2. Postorder");
                    System.out.println("3. Preorder");
                    userIn = getUserInput();
                    if(userIn == 1) tree.inOrder();
                    else if(userIn == 2) tree.postOrder();
                    else if(userIn == 3) tree.preOrder();
                    else System.out.println("Invalid Input! Returning to Main Menu...");
                    System.out.println("");
                    break;
                case 4:
                    System.out.println(tree.treeToString((RBTree.RBTreeNode) tree.root));
                    break;
                case 0:
                    keepGoing = false;
                    break;
                default:
                    System.out.println("Invalid Entry ");
            }
        }

    }
    public static int getUserInput(){
        boolean invalidUserIn = true;
        int userIn = 0;
        while(invalidUserIn){
            try{
                userIn = scanner.nextInt();
                scanner.nextLine();
                invalidUserIn = false;
            }catch(Exception e){
                System.out.println("Must be an Integer! ");
                scanner.nextLine();
            }
        }
        return userIn;
    }
}
