//Carl Owen
//Lab 1 Due Wed Sep 4 2024

/*This program is called Making change it is a program set
to simulate a simple cash register, while displaying the fewest amount of coins
in the change making process, the user inputs an amount
Each class uses a specific task
-Denomination representing the records of each of the amounts bills/coins
-Purse storing and managing
-Register that contains all logic to calculate
-GUI to display to the denominations through Jframe with a register panel
*/

//Hello World
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The main class which also contains the
//main method and serves as the entry point for the GUI
public class MakingChange
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Making Change");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        RegisterPanel panel = new RegisterPanel(new Register());
        frame.add(panel);

        frame.setVisible(true);
    }
}

//Record to represent a Denomination
record Denomination(String name, double amt, String form, String img) {}

//Class to represent a Purse containing different denominations
class Purse
{
    private Map<Denomination, Integer> cash = new HashMap<>();

    public void add(Denomination type, int num)
    {
        //Updates map by adding specific number if not there starting at 0
        cash.put(type, cash.getOrDefault(type, 0) + num);
    }

    public double remove(Denomination type, int num)
    {
        //Checks if the purse contains the specific denominations
        if (cash.containsKey(type))
        {
            //Gets current num in purse
            int current = cash.get(type);
            //Finds what num to remove
            int toRemove = Math.min(current, num);
            cash.put(type, current - toRemove);
            //Returns back what was removed in totatality
            return toRemove * type.amt();
        }
        return 0;
    }

    public double getValue()
    {
        return cash.entrySet().stream()
                .mapToDouble(e -> e.getKey().amt() * e.getValue())
                .sum();
    }

    public Map<Denomination, Integer> getCash()
    {
        return cash;
    }

    @Override
    public String toString()
    {
        //Creates a string builder for purse contents
        StringBuilder sb = new StringBuilder("Purse contents:\n");
        cash.forEach((denomination, count) -> {
            sb.append(count).append(" x ").append(denomination.name()).append(" (")
                    .append(denomination.form()).append(") - $").append(denomination.amt()).append("\n");
        });
        return sb.toString();
    }
}

//This class is set to represent the Register function
//which has the logic for making change
class Register
{
    private static final List<Denomination> DENOMINATIONS = List.of(
            new Denomination("Hundred Dollar Bill", 100.0, "bill", "hundred.png"),
            new Denomination("Fifty Dollar Bill", 50.0, "bill", "fifty.png"),
            new Denomination("Twenty Dollar Bill", 20.0, "bill", "twenty.png"),
            new Denomination("Ten Dollar Bill", 10.0, "bill", "ten.png"),
            new Denomination("Five Dollar Bill", 5.0, "bill", "five.png"),
            new Denomination("One Dollar Bill", 1.0, "bill", "one.png"),
            new Denomination("Quarter", 0.25, "coin", "quarter.png"),
            new Denomination("Dime", 0.10, "coin", "dime.png"),
            new Denomination("Nickel", 0.05, "coin", "nickel.png"),
            new Denomination("Penny", 0.01, "coin", "penny.png")
    );
//Calculates fewest number of bills/coins
    public Purse makeChange(double amt)
    {
        //Creates new purse to store bills
        Purse purse = new Purse();
        for (Denomination denomination : DENOMINATIONS)
        {
            int count = (int) (amt / denomination.amt());
            if (count > 0)
            {
                purse.add(denomination, count);
                amt -= count * denomination.amt();
            }
        }
        return purse;
    }
}

//Panel extension to represent the panel
//where the user interacts with the Register
class RegisterPanel extends JPanel
{
    private Register register;
    private JTextField input;
    private PursePanel changePanel;

    public RegisterPanel(Register register)
    {
        this.register = register;

        setLayout(new BorderLayout());

        input = new JTextField(10);
        changePanel = new PursePanel();

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter amount: "));
        inputPanel.add(input);

        input.addActionListener(new InputListener());

        add(inputPanel, BorderLayout.NORTH);
        add(changePanel, BorderLayout.CENTER);
    }
//Inner class to do user input
    private class InputListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Analyze the text given
            double amount = Double.parseDouble(input.getText());
            //Uses the register to make change and store in purse
            Purse purse = register.makeChange(amount);
            //Update purse
            changePanel.setPurse(purse);
        }
    }
}

//Represents the display of the contents of the Purse
class PursePanel extends JPanel
{
    private Purse purse = new Purse();
//Calls for the repaint method to trigger the paint component
    public void setPurse(Purse purse)
    {
        this.purse = purse;
        repaint();
    }

//Override is included to signal that the paintComponent with override
//the Jpanel class, it helps with compiling
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //Starts vertical position of the text
        int y = 20;
        for (Map.Entry<Denomination, Integer> entry : purse.getCash().entrySet())
        {
            //Should draw the denominations
            g.drawString(entry.getValue() + " x " + entry.getKey().name(), 10, y);
            y += 20;
        }
    }
}

