package timetogether.where2meet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
import timetogether.group.repository.GroupRepository;
import timetogether.group.service.GroupService;
import timetogether.GroupWhere.repository.GroupWhereQueryRepository;
import timetogether.GroupWhere.repository.GroupWhereRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class Where2meetService {
  private final GroupRepository groupRepository;
  private final GroupService groupService;
  private final GroupWhereQueryRepository groupWhereQueryRepository;
  private final GroupWhereRepository groupWhereRepository;


}
