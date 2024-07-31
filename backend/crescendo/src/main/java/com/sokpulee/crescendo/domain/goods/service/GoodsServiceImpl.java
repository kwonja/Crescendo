package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.GoodsImage;
import com.sokpulee.crescendo.domain.goods.repository.GoodsRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolGroupNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService{
    private final UserRepository userRepository;

    private final IdolGroupRepository idolGroupRepository;

    private final FileSaveHelper fileSaveHelper;

    private final GoodsRepository goodsRepository;


    @Override
    public void addGoods(Long loggedInUserId, GoodsAddRequest goodsAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        IdolGroup idolGroup = idolGroupRepository.findById(goodsAddRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);

        Goods goods = Goods.builder()
                .idolGroup(idolGroup)
                .user(user)
                .title(goodsAddRequest.getTitle())
                .content(goodsAddRequest.getContent())
                .build();

        for(MultipartFile goodsImageFile : goodsAddRequest.getImageList()){
            String savePath = fileSaveHelper.saveGoodsImage(goodsImageFile);

            GoodsImage goodsImage = GoodsImage.builder()
                    .imagePath(savePath)
                    .build();
            System.out.println(goodsImage.toString());

            goods.addImage(goodsImage);
        }

        goodsRepository.save(goods);
    }
}
