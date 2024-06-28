package com.zrh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.entity.Profession;
import com.zrh.service.ProfessionService;
import com.zrh.mapper.ProfessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【profession】的数据库操作Service实现
 * @createDate 2024-03-25 13:58:04
 */
@Service
public class ProfessionServiceImpl extends ServiceImpl<ProfessionMapper, Profession>
        implements ProfessionService {
    @Autowired
    private ProfessionMapper professionMapper;

    @Override
    public List<Profession> getProfessionList() {
        return professionMapper.selectList(null);
    }
}




