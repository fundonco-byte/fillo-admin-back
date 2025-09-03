package sh.admin.backend.common.vo;

import sh.admin.backend.common.util.StringUtil;

public class DataMapCamelCaseVO extends DataMapVO{
	private static final long serialVersionUID = 1512568704260345606L;
	
    @Override
    public Object put(String key, Object value) {
        return super.put(StringUtil.toCamelCase(key), value);
    }
}
