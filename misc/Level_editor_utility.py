import tkinter as tk
from tkinter import messagebox
from tkinter import filedialog

# Function to handle button click events
def button_click(row, col):
    button = buttons[row][col]
    text = button["text"]
    
    if text == "N":
        button["text"] = "W"
        button.configure(bg="red")
    elif text == "W":
        button["text"] = "WD"
        button.configure(bg="orange")
    else:
        button["text"] = "N"
        button.configure(bg="green")


# Function to copy button values to the textbox
def copy_values():
        
    textbox.delete("1.0", tk.END)  # Clear the textbox
    
    # Iterate over all buttons and append their values to the textbox
    for row in range(rows):
        for col in range(cols):
            button = buttons[col][row]
            textbox.insert(tk.END, button["text"])
            textbox.insert(tk.END, ",")
        textbox.insert(tk.END, "\n")

def set_grid():
    for row in range(rows):
        for col in range(cols):
            if (row != 0 and col != 0 and row < 16 and col < 14 and row % 2 == 0 and col % 2 == 0):
                button_click(row, col)

def save_to_file():
    filename = "levels/" + filename_entry.get()
    filename += ".lvl"
    text = textbox.get("1.0", tk.END)
    with open(filename, "w") as file:
        file.write(text)
    messagebox.showinfo("File Saved", f"The file has been saved as {filename}")
    
def open_file():
    file_path = filedialog.askopenfilename(filetypes=[("Level files", "*.lvl")])
    if file_path:
        with open(file_path, "r") as file:
            content = file.read()
        textbox.delete("1.0", tk.END)
        textbox.insert(tk.END, content)
        update_buttons(content)

def update_buttons(content):
    lines = content.strip().split("\n")
    for row in range(len(lines)):
        values = lines[row].split()
        for col in range(len(values)):
            button = buttons[col][row]
            button["text"] = values[col]
            if values[col] == "N":
                button.configure(bg="green")
            elif values[col] == "W":
                button.configure(bg="red")
            elif values[col] == "WD":
                button.configure(bg="orange")

# Create the main window
window = tk.Tk()
window.title("Button Matrix")

rows = 15
cols = 17

# Create a 2D list to store the buttons
buttons = []
for col in range(cols):
    button_row = []
    for row in range(rows):
        button = tk.Button(window, text="N", width=2, height=1,
                           command=lambda r=row, c=col: button_click(c, r))
        button.grid(row=row, column=col, padx=0, pady=0)
        button_row.append(button)
        button.configure(bg="green")  # Set initial button color to green
    buttons.append(button_row)
    
# set the buttons on the edges to red and a value of W
for col in range(cols):
    buttons[col][0]["text"] = "W"
    buttons[col][0].configure(bg="red")
    
    buttons[col][-1]["text"] = "W"
    buttons[col][-1].configure(bg="red")
# do the same for row
for row in range(rows):
   buttons[0][row]["text"] = "W"
   buttons[0][row].configure(bg="red")
   buttons[-1][row]["text"] = "W"
   buttons[-1][row].configure(bg="red")

# Create the button and textbox for copying values
copy_button = tk.Button(window, text="Copy Values", command=copy_values)
copy_button.grid(row=rows, columnspan=cols, pady=10)

grid_button = tk.Button(window, text="Grid", command=set_grid)
grid_button.grid(row=rows+1, columnspan=cols, padx=50, pady=10)

textbox = tk.Text(window, height=5, width=55)
textbox.grid(row=rows+2, columnspan=cols)

# Create the filename entry textbox and label
filename_label = tk.Label(window, text="File Name:")
filename_label.grid(row=rows+3, columnspan=cols-2, pady=10)

filename_entry = tk.Entry(window, width=20)
filename_entry.grid(row=rows+3, columnspan=cols, pady=10)

# Create the button for saving to a file
save_button = tk.Button(window, text="Save to File", command=save_to_file)
save_button.grid(row=rows+4, columnspan=cols, pady=10)

# Create the button for opening and pasting a file
open_button = tk.Button(window, text="Open File", command=open_file)
open_button.grid(row=rows+5, columnspan=cols, pady=10)

window.mainloop()
