package zzh.darfing.mycrm.settings.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.DicValue;
import zzh.darfing.mycrm.settings.pojo.DictionaryType;
import zzh.darfing.mycrm.settings.web.service.DictionaryTypeService;
import zzh.darfing.mycrm.settings.web.service.DictionaryValueService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/settings/dictionary")
public class DictionaryController {

    @Autowired
    DictionaryTypeService dictionaryTypeService;
    @Autowired
    DictionaryValueService dictionaryValueService;

    @RequestMapping("/type/index.do")
    public String typeIndex(HttpServletRequest request) {
        List<DictionaryType> dictionaryTypes = dictionaryTypeService.queryAllDictionaryType();
        request.setAttribute("typeList", dictionaryTypes);
        return "/settings/dictionary/type/index";
    }

    @RequestMapping("/index.do")
    public String toIndex() {
        return "/settings/dictionary/index";
    }

    @RequestMapping("/type/toEditDictionaryType.do/{code}")
    public String toEditDictionaryTypePage(HttpServletRequest request, @PathVariable("code") String code) {
        DictionaryType dictionaryType = dictionaryTypeService.queryDictionaryTypeByCode(code);
        request.setAttribute("editType", dictionaryType);
        return "/settings/dictionary/type/edit";
    }

    @RequestMapping("/type/checkCodeIsExist.do/{code}")
    @ResponseBody
    public Object checkCodeIsExist(@PathVariable("code") String code) {
        Result result = new Result();
        try {
            DictionaryType dictionaryType = dictionaryTypeService.queryDictionaryTypeByCode(code);
            if(null == dictionaryType) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器异常!!!");
            throw new RuntimeException(e);
        }
        return result;
    }

    @RequestMapping("/type/toCreateDictionaryType.do")
    public String toCreateDictionaryType() {
        return "/settings/dictionary/type/save";
    }

    @RequestMapping("/saveEditedDictionaryType.do")
    @ResponseBody
    public Object saveEditedDictionaryType(DictionaryType dictionaryType) {
        Result result = new Result();
        try {
            int i = dictionaryTypeService.saveEditDictionaryType(dictionaryType);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/saveCreatedDictionaryType.do")
    @ResponseBody
    public Object saveCreatedDictionaryType(DictionaryType dictionaryType) {
        Result result = new Result();
        try {
            int i = dictionaryTypeService.saveCreateDictionaryType(dictionaryType);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/deleteDictionaryType.do")
    @ResponseBody
    public Object deleteDictionaryType(String[] code) {
        Result result = new Result();
        try {
            int i = dictionaryTypeService.deleteDictionaryTypeByCodes(code);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/value/index.do")
    public String valueIndex(HttpServletRequest request) {
        List<DicValue> dicValues = dictionaryValueService.queryAllDicValue();
        request.setAttribute("valueList", dicValues);
        return "/settings/dictionary/value/index";
    }


    @RequestMapping("/value/toEditDictValue.do/{id}")
    public String toEditDictValuePage(HttpServletRequest request, @PathVariable("id") String id) {
        DicValue dicValue = dictionaryValueService.queryDicValueById(id);
        request.setAttribute("dicValue", dicValue);
        return "/settings/dictionary/value/edit";
    }



    @RequestMapping("/value/toCreateDicValue.do")
    public String toCreateDicValue(HttpServletRequest request) {
        List<DictionaryType> dictionaryTypes = dictionaryTypeService.queryAllDictionaryType();
        List<String> codes = new ArrayList<>();
        for (DictionaryType dictionaryType : dictionaryTypes) {
            codes.add(dictionaryType.getCode());
        }
        request.setAttribute("codes", codes);
        return "/settings/dictionary/value/save";
    }

    @RequestMapping("/saveEditedDicValue.do")
    @ResponseBody
    public Object saveEditedDicValue(DicValue dictionaryType) {
        Result result = new Result();
        try {
            int i = dictionaryValueService.saveEditDicValue(dictionaryType);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/saveCreatedDicValue.do")
    @ResponseBody
    public Object saveCreatedDicValue(DicValue dictionaryType) {
        Result result = new Result();
        dictionaryType.setId(UUIDUtil.getUUID());
        try {
            int i = dictionaryValueService.saveCreateDicValue(dictionaryType);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/deleteDicValue.do")
    @ResponseBody
    public Object deleteDicValue(String[] id) {
        Result result = new Result();
        try {
            int i = dictionaryValueService.deleteDicValueByIds(id);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }



}
