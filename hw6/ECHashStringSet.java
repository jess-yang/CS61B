import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Jessica Yang
 */
class ECHashStringSet implements StringSet {
    public ECHashStringSet() {
        _load = 5;
        _size = 0;
        _all = new ArrayList[_load];
        for (int i = 0; i < _load; i++) {
            _all[i] = new ArrayList<>();
        }
    }

    @Override
    public boolean contains(String s) {
        if (s == null) {
            return false;
        } else {
            int hc = s.hashCode();
            if (hc < 0)
                hc = (hc & 0x7fffffff);
            return _all[hc % _load].contains(s);
        }
    }

    @Override
    public void put(String s) {
        if (s == null) {
            return;
        } else if ((double) _size / _load > 5.0) {
            ArrayList<String>[] old = _all;
            _load = _load * 2;
            _all = new ArrayList[_load];
            for (int i = 0; i < _load; i++) {
                _all[i] = new ArrayList<>();
            }
            for (ArrayList<String> hs: old) {
                for (String t: hs) {
                    put(t);
                }
            }
        }
        int hc = s.hashCode();
        if (s.hashCode() < 0) {
            hc = (s.hashCode() & 0x7fffffff);
        }
        _all[hc % _load].add(s);
        _size++;
    }

    @Override
    public List<String> asList() {
        List<String> ret = new ArrayList<>();
        for (ArrayList<String> hs: _all) {
            for (String s: hs) {
                ret.add(s);
            }
        }
        return ret;
    }

    private int _load;
    private int _size;
    private ArrayList<String>[] _all;
}
