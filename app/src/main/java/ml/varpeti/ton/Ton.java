/*
MIT License
Copyright (c) 2019 Váraljai Péter
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package ml.varpeti.ton;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ton implements Serializable, Cloneable
{
    private HashMap<String,Ton> keyvalues;

    //*/// Constructors

    public Ton()
    {
        keyvalues = new HashMap<>();
    }

    public Ton(String key)
    {
        keyvalues = new HashMap<>();
        this.put(key,new Ton());
    }

    public Ton(String key, String value)
    {
        this.put(key,value);
    }

    //*/// Normal HasMap manipulation

    public boolean containsKey(String key)
    {
        return keyvalues.containsKey(key);
    }

    public Ton get(String key)
    {
        return keyvalues.get(key);
    }

    //Private cos no need to create a self contained mess.
    private void put(String key, Ton ton)
    {
        keyvalues.put(key,ton);
    }

    public Set<String> keySet()
    {
        return keyvalues.keySet();
    }

    public Collection<Ton> values()
    {
        return keyvalues.values();
    }

    public String first()
    {
        return keyvalues.keySet().iterator().next();
    }

    public Boolean isEmpty()
    {
        return keyvalues.isEmpty();
    }

    public void remove(String key)
    {
        keyvalues.remove(key);
    }

    //*/// Extra HasMap manipulation

    // If the key already exist don't overwrite, also has no return value
    public void put(String key)
    {
        if (this.containsKey(key))
            return;
        else
            this.put(key,new Ton());
    }

    // If the key already exist put another value, also has no return value
    public void put(String key, String value)
    {
        if (this.containsKey(key))
            this.get(key).put(value);
        else
            this.put(key,new Ton(value));
    }

    public void putAll(String key, Collection<String> values)
    {
        for (String v : values)
        {
            this.put(key,v);
        }
    }

    public ArrayList<String> getKeyArrayList()
    {
        ArrayList<String> ret = new ArrayList<>();
        int i = 0;
        while (keyvalues.containsKey(""+i))
        {
            Ton num = keyvalues.get(""+i);
            if (!num.isEmpty()) ret.add(num.first());
            i++;
        }
        return ret;
    }

    public ArrayList<Ton> getValueArrayList()
    {
        ArrayList<Ton> ret = new ArrayList<>();
        int i = 0;
        while (keyvalues.containsKey(""+i))
        {
            Ton num = keyvalues.get(""+i);
            if (!num.isEmpty()) ret.add(num.get(num.first()));
            i++;
        }
        return ret;
    }

    //

    public void putParsedfromFile(String key, String fileName) throws IOException
    {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try
        {
            this.put(key,parsefromString(bufferedReader));
        }
        finally
        {
            bufferedReader.close();
        }
    }

    public void putParsedfromString(String key, String raw) throws IOException
    {
        InputStream is = new ByteArrayInputStream(raw.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        this.put(key,parsefromString(bufferedReader));
    }

    public void putParsedfromString(String key, BufferedReader bufferedReader) throws IOException
    {
        this.put(key,parsefromString(bufferedReader));
    }

    //*/// Statics

    static public Ton parsefromFile(String fileName) throws IOException
    {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try
        {
            return parsefromString(bufferedReader);
        }
        finally
        {
            bufferedReader.close();
        }
    }

    static public Ton parsefromString(String raw) throws IOException
    {
        InputStream is = new ByteArrayInputStream(raw.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        return parsefromString(bufferedReader);
    }

    static public Ton parsefromString(BufferedReader bufferedReader) throws IOException
    {
        Ton ret = new Ton();

        Stack<Integer> stack = new Stack<>();
        HashMap<Integer,String> hash = new HashMap<>();
        Integer last = -1;

        String line = null;
        while((line = bufferedReader.readLine()) != null)
        {
            Pattern p = Pattern.compile("^([\t ]*)([^\t ]*.*)$"); // (tabs or spaces)(others)
            Matcher m = p.matcher(line);
            if (m.find())
            {
                Integer t = m.group(1).length(); //Number of indentation chars
                String value = m.group(2);
                //System.out.println(t+" "+value);

                hash.put(t,value);

                if (t>last)
                {
                    stack.push(t);
                }
                else if (t<last)
                {
                    while (stack.size()>0 && stack.lastElement()>t)
                    {
                        stack.pop();
                    }
                }
                last = t;

                Ton a = ret;
                ArrayList<Integer> list = new ArrayList(stack);
                for (Integer tt : list)
                {
                    String key = hash.get(tt);
                    if (!a.containsKey(key))
                    {
                        a.put(key);
                    }
                    a = a.get(key);
                    //System.out.println(hash.get(tt));
                }
                //System.out.println();

            }
        }
        return ret;
    }

    static private String ton2String(Ton ton,int lvl)
    {
        StringBuilder ret = new StringBuilder();
        String t = new String(new char[lvl]).replace("\0", "\t"); // Until String::repeate

        for(String key : ton.keySet() )
        {
            ret.append(t+key+"\n"+ton2String(ton.get(key),lvl+1)); //RECURSION!!!
        }

        return ret.toString();
    }

    @Override
    public String toString()
    {
        return ton2String(this,0);
    }
}