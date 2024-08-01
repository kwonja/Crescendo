package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.feed.entity.Feed;
import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.GoodsComment;
import com.sokpulee.crescendo.domain.goods.entity.GoodsImage;
import com.sokpulee.crescendo.domain.goods.repository.GoodsCommentRepository;
import com.sokpulee.crescendo.domain.goods.repository.GoodsRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final UserRepository userRepository;

    private final IdolGroupRepository idolGroupRepository;

    private final FileSaveHelper fileSaveHelper;

    private final GoodsRepository goodsRepository;

    private final GoodsCommentRepository goodsCommentRepository;


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

        if (!goodsAddRequest.getImageList().isEmpty()) {
            for (MultipartFile goodsImageFile : goodsAddRequest.getImageList()) {
                String savePath = fileSaveHelper.saveGoodsImage(goodsImageFile);

                GoodsImage goodsImage = GoodsImage.builder()
                        .imagePath(savePath)
                        .build();
                System.out.println(goodsImage.toString());

                goods.addImage(goodsImage);
            }
        }

        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long loggedInUserId, Long goodsId) {
        if (goodsRepository.existsById(goodsId)) {
            goodsRepository.deleteById(goodsId);
        } else {
            throw new GoodsNotFoundException();
        }
    }

    @Override
    public void addGoodsComment(Long loggedInUserId, Long goodsId, GoodsCommentAddRequest goodsCommentAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        GoodsComment goodsComment = GoodsComment.builder()
                .goods(goods)
                .user(user)
                .content(goodsCommentAddRequest.getContent())
                .build();

        goodsCommentRepository.save(goodsComment);
    }

    @Override
    public void addGoodsReply(Long loggedInUserId, Long goodsId, Long goodsCommentId, GoodsCommentAddRequest goodsReplyAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        GoodsComment parentGoodsComment = goodsCommentRepository.findById(goodsCommentId)
                .orElseThrow(GoodsCommentNotFoundException::new);

        GoodsComment goodsComment = GoodsComment.builder()
                .goods(goods)
                .parentGoodsComment(parentGoodsComment)
                .user(user)
                .content(goodsReplyAddRequest.getContent())
                .build();

        if (parentGoodsComment.getParentGoodsComment() == null) {
            goodsCommentRepository.save(goodsComment);
        } else {
            throw new GoodsCommentNotFoundException();
        }
    }


}
