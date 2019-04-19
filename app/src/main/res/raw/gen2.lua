local n =
{
    {"0",   "§",    "$",    "€" },
    {"1",   "'",    "~",    "ß" },
    {"2",   '"',    "ˇ",    "¤" },
    {"3",   "+",    "^",    "÷" },
    {"4",   "!",    "˘",    "×" },
    {"5",   "%",    "°",    "¸" },
    {"6",   "/",    "˛",        },
    {"7",   "=",    "`",        },
    {"8",   "(",    "˙",        },
    {"9",   ")",    "´",        },
    {"q",   "\\",   "Q",        },
    {"w",   "|",    "W",        },
    {"e",   "é",    "E",    "É" },
    {"r",   "¨",    "R",    "˝" },
    {"t",   "ü",    "T",    "Ü" },
    {"z",   "ű",    "Z",    "Ű" },
    {"u",   "ú",    "U",    "Ú" },
    {"i",   "í",    "I",    "Í" },
    {"o",   "ó",    "O",    "Ó" },
    {"p",   "¸",    "P",        },
    {"a",   "á",    "A",    "Á" },
    {"s",   "đ",    "S",        },
    {"d",   "Đ",    "D",        },
    {"f",   "[",    "F",        },
    {"g",   "]",    "G",        },
    {"h",   "ö",    "H",    "Ö" },
    {"j",   "ő",    "J",    "Ő" },
    {"k",   "ł",    "K",        },
    {"l",   "Ł",    "L",        },
    {"y",   ">",    "Y",        },
    {"x",   "#",    "X",        },
    {"c",   "&",    "C",        },
    {"v",   "@",    "V",        },
    {"b",   "{",    "B",        },
    {"n",   "}",    "N",        },
    {"m",   "<",    "M",        },
    {",",   ";",                },
    {".",   ":",                },
    {"-",   "_",                },
    {"?",   "*",                },
}

for i,v in ipairs(n) do
    if v[1] then
        print(v[1])
        print("\tshow")
        print("\t\tprimary")
        print("\t\t\t"..v[1])

        if v[2] then 
            print("\t\tsecondary")
            print("\t\t\t"..v[2])
        end
        print("\tcmd") 
        print("\t\tnormal")
        print("\t\t\t0")
        print("\t\t\t\tprint")
        print("\t\t\t\t\t"..v[1])
        if v[2] then 
            print("\t\tlong")
            print("\t\t\t0")
            print("\t\t\t\tprint")
            print("\t\t\t\t\t"..v[2])
        end
    end

    if v[3] then --shift
        print("s"..v[1])
        print("\tshow")
        print("\t\tprimary")
        print("\t\t\t"..v[3])
        if v[4] then 
            print("\t\tsecondary")
            print("\t\t\t"..v[4])
        end
        print("\tcmd") 
        print("\t\tnormal")
        print("\t\t\t0")
        print("\t\t\t\tprint")
        print("\t\t\t\t\t"..v[3])
        print("\t\t\t1")
        print("\t\t\t\tswitch")
        print("\t\t\t\t\tmain")
        if v[4] then 
            print("\t\tlong")
            print("\t\t\t0")
            print("\t\t\t\tprint")
            print("\t\t\t\t\t"..v[4])
            print("\t\t\t1")
            print("\t\t\t\tswitch")
            print("\t\t\t\t\tmain")
        end
    end

    if v[3] then --capslocks
        print("c"..v[1])
        print("\tshow")
        print("\t\tprimary")
        print("\t\t\t"..v[3])
        if v[4] then 
            print("\t\tsecondary")
            print("\t\t\t"..v[4])
        end
        print("\tcmd") 
        print("\t\tnormal")
        print("\t\t\t0")
        print("\t\t\t\tprint")
        print("\t\t\t\t\t"..v[3])
        if v[4] then 
            print("\t\tlong")
            print("\t\t\t0")
            print("\t\t\t\tprint")
            print("\t\t\t\t\t"..v[4])
        end
    end
end

local oth =
[[shiftin
    show
        primary
            ⇧
    cmd
        normal
            0
                switch
                    shift
        long
            0
                switch
                    caps
shiftout
    show
        primary
            ⇩
    cmd
        normal
            0
                switch
                    main
        long
            0
                switch
                    caps
ctrl
    show
        primary
                ctrl
    cmd
        normal
            0
                ctrl
        long
            0
                switch
                    smiley
space
    show
        size
            2
    cmd
        normal
            0
                keycode
                    62
        long
            0
                keycode
                    61
backspace
    show
        primary
            ⟵
    cmd
        normal
            0
                keycode
                    67
        long
            0
                delete
                    word
return
    show
        primary
            ⤶
    cmd
        normal
            0
                keycode
                    66
        long
            0
                settings
cursorleft
    show
        primary
            ←
    cmd
        normal
            0
                keycode
                     21
cursordown
    show
        primary
            ↓
    cmd
        normal
            0
                keycode
                     20
cursorup
    show
        primary
            ↑
    cmd
        normal
            0
                keycode
                     19
cursorright
    show
        primary
            →
    cmd
        normal
            0
                keycode
                     22
escapce
    show
        primary
            esc
    cmd
        normal
            0
                keycode
                     111
smiley
    show
        primary
            ☺
    cmd
        normal
            0
                switch
                    smiley
voice
    show
        primary
            🎤
    cmd
        normal
            0
                voice
settings
    show
        primary
            ⚙
    cmd
        normal
            0
                settings]]
print(oth)