# CKeyBoarDD

## TON Dokumentáció

### b.ton

Ebben definiáljuk a gombokat.

Példa:
```
shift:e
    show
        primary
            E
        secondary
            É
    cmd
        normal
            0
                print
                    E
            1
                switch
                    main
        long
            0
                print
                    É
            1
                switch
                    main
```

- `"gombID"`
    + A gomb ID-je. Egyedi kell legyen. Space lehet benne, csak elöte és utánna nem.
    + Lehetséges értékek: `show, cmd, settings`
    + `show`
        * Lehetséges értékek: `primary, secondary`
        * `primary`
            - Ez az elsődleges szöveg a gombon. Ez utal a gomb normál nyomásakor történő eseméynekre.
            - opcionális
        * `secondary` 
            - Ez a másodlagos szöveg a gombon. Ez utal a gomb hosszú nyomásakor történő eseméynekre.
            - opcionáls
    + `cmd`
        * Lehetséges értékek: `normal, long`
        * `normal` 
            - Ezeket a parancsokat hajta végre sorba, 0-tól a legnagyobbig, a gomb normális megnyomásakor.
            - Lehetséges parancsok: `print, hit, keycode, ctrl, delete, settings, voice`
        * `long` 
            - Ezeket a parancsokat hajta végre sorba, 0-tól a legnagyobbig, a gomb hosszan való megnyomásakor.
            - opcionáls
            - Ha nincs definiálva, akkor a `normal` nyomást ismétli, állítható időközönként.
            - Lehetséges parancsok: `print, hit, keycode, ctrl, switch, delete, settings`
            - Bővebben lentebb
    + `settings`
        * Lehetséges értékek: `Height, Width, HorizontalMargin, VerticalMargin, PrimaryTextSize, SecondaryTextSize, PrimaryTextColor, SecondaryTextColor, ButtonBackgroundColor, RepeatInitialInterval, RepeatInterval`
        * opcionáls
        * Bővebben lentebb

#### Parancsok (`cmd`)

- `print`
    + Kiírja az egy szintel utánna írt értéket
- `hit`
    + Kiírja az egy szintel utánna írt értéket, úgy hogy szimulálja mintha fizikailag le lett volna ütve.
- `keycode`
    + Kiírja az egy szintel utánna írt érték karakter kódját
- `ctrl`
    + Bekapcsolja a ctrl módot, a legközelebbi karakter leütéshez hozzáadja ha tudja, és kikapcsolja.
- `switch`
    + Átvált az egy szintel utánna írt `"billentyűzetID"`-jű billentyűzetre, ha létezik.
- `delete`
    + Legetséges értékek: `word, all`
        * `word`
            - Visszatörli az utolsó szót amiben vagy elött a kurzor áll.
        * `all`
            - Kitöröl mindent.
- `settings` 
    + Megnyitja  a beállításokat.

### r.ton

Ebben definiáljuk a sorokat.

Példa:
```
normal:a1
    buttons
        0
            normal:q
        1
            normal:w
        2
            normal:e
        3
            normal:r
        4
            normal:t
        5
            normal:z
        6
            normal:u
        7
            normal:i
        8
            normal:o
        9
            normal:p
```

- `"sorID"`
    + A sor ID-je. Egyedi kell legyen. Space lehet benne, csak elöte és utánna nem.
    + Lehetséges értékek: `buttons, settings`
    + `buttons`
        * Ezek a gombok sorban, a 0-tól, a `"gombID"`-vel azonosítva.
    + `settings`
        * Lehetséges értékek: `Height, Width, HorizontalMargin, VerticalMargin, PrimaryTextSize, SecondaryTextSize, PrimaryTextColor, SecondaryTextColor, ButtonBackgroundColor, RowBackgroundColor, RepeatInitialInterval, RepeatInterval`
        * opcionáls
        * Bővebben lenteb

### k.ton

Ebben definiáljuk a billentyűzeteket.

Példa:
```
main
    settings
        Height
            50
    rows
        0
            normal:num
        1
            normal:a1
        2
            normal:a2
        3
            normal:a3
        4
            normal:a4
```

- `"billentyűzetID"`
    + A billentyűzet ID-je. Egyedi kell legyen. Space lehet benne, csak elöte és utánna nem. A "main" az kitüntetett az töltődik be elsőnek. Ha nincs "main" akkor a legelső.
    + Lehetséges értékek: `rows, settings`
    + `row`
        * Ezek a sorok sorban, a 0-tól, a `"rowID"`-vel azonosítva.
    + `settings`
        * Lehetséges értékek: `Height, Width, HorizontalMargin, VerticalMargin, PrimaryTextSize, SecondaryTextSize, PrimaryTextColor, SecondaryTextColor, ButtonBackgroundColor, RowBackgroundColor, KeyboardBackgroundColor, RepeatInitialInterval, RepeatInterval`
        * opcionáls
        * Bővebben lenteb

### Beállítások (`settings`)

Lentről felfele érvényesül: ha nincs definiálva a gombnál akkor a soré, ha a sornál sincs akkor a billenytűzeté, ha a billenytűzeté sincs akkor az alapértelmezett beállítás lesz érvényben.

- `Height`
    + A gomb magassága DIP ben.
- `Width`
    + A gomb szélessége alapértelmezetten 1.0, ha nagyobb akkor a többi gomhoz képest szélesebb lesz.
- `HorizontalMargin`
    + A gombok közötti vízszintes távolság PX ben.
- `VerticalMargin`
    + A sorok közötti függőleges távolság PX ben.
- `PrimaryTextSize`
    + Az elsődleges szöveg nagysága SP ben.
- `SecondaryTextSize`
    + A másodlagos szöveg naagysága SP ben.
- `PrimaryTextColor`
    + Az elsődleges szöveg színe "#RRGGBB" formátumban.
- `SecondaryTextColor`
    + Az elsődleges szöveg színe "#RRGGBB" formátumban.
- `ButtonBackgroundColor`
    + Az gomb háttérszíne "#RRGGBB" formátumban.
- `RowBackgroundColor`
    + Az sor háttérszíne "#RRGGBB" formátumban. Ezek a függőleges csíkok.
- `KeyboardBackgroundColor`
    + Az billenytűzet háttérszíne "#RRGGBB" formátumban. Ezek a vízszintes csíkok.
- `RepeatInitialInterval`
    + Ha nincs definiálva a hosszú (`long`) parancs akkor ennyi MS múlva kezdi el az ismétlést.
- `RepeatInterval`
    + Ennyi időnként ismétli meg a parancso(kat) ha nyomva van tartva egy gomb.




