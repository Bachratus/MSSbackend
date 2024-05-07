package com.mss.app.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mss.app.domain.DefaultTask;
import com.mss.app.domain.SubprojectType;
import com.mss.app.domain.Task;
import com.mss.app.domain.TaskReport;
import com.mss.app.domain.User;
import com.mss.app.domain.keys.UserTaskKey;
import com.mss.app.enums.WorkDayStatus;
import com.mss.app.error.BadRequestAlertException;
import com.mss.app.repository.DefaultTaskRepository;
import com.mss.app.repository.SubprojectTypeRepository;
import com.mss.app.repository.TaskReportRepository;
import com.mss.app.repository.TaskRepository;
import com.mss.app.repository.UserRepository;
import com.mss.app.security.SecurityUtil;
import com.mss.app.service.TaskReportService;
import com.mss.app.service.TaskService;
import com.mss.app.service.dto.TaskReportDTO;
import com.mss.app.service.dto.UserReportDTO;
import com.mss.app.service.dto.weekly_report_dtos.DailyReportDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklySummaryDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklyTaskReportDTO;
import com.mss.app.service.mapper.TaskReportMapper;
import com.mss.app.tools.FreeDays;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TaskReportServiceImpl implements TaskReportService {
    private final Logger log = LoggerFactory.getLogger(TaskReportServiceImpl.class);

    private final TaskReportRepository taskReportRepository;

    private final DefaultTaskRepository defaultTaskRepository;

    private final TaskReportMapper taskReportMapper;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    private final SubprojectTypeRepository subprojectTypeRepository;

    public TaskReportServiceImpl(
            TaskReportRepository taskReportRepository,
            DefaultTaskRepository defaultTaskRepository,
            UserRepository userRepository,
            TaskReportMapper taskReportMapper,
            TaskRepository taskRepository,
            TaskService taskService,
            SubprojectTypeRepository subprojectTypeRepository) {
        this.taskReportRepository = taskReportRepository;
        this.defaultTaskRepository = defaultTaskRepository;
        this.userRepository = userRepository;
        this.taskReportMapper = taskReportMapper;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.subprojectTypeRepository = subprojectTypeRepository;
    }

    @Override
    public List<UserReportDTO> getMonthlyUserReport(LocalDate fromDate, LocalDate toDate) {
        User user = SecurityUtil
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin).get();
        List<TaskReportDTO> rawReports = this.taskReportMapper
                .toDtoList(this.taskReportRepository.findAllByUserIdAndDateBetweenOrderByDateDesc(user.getId(),
                        fromDate, toDate));

        List<LocalDate> datesInRange = new ArrayList<>();
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            datesInRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        List<UserReportDTO> preparedReports = new ArrayList<UserReportDTO>();

        for (LocalDate date : datesInRange) {
            List<TaskReportDTO> reportsForDate = rawReports.stream()
                    .filter(report -> report.getDate().equals(date))
                    .collect(Collectors.toList());
            Double summedHours = reportsForDate.stream()
                    .filter(report -> report.getType() != 7
                            && report.getType() != 8)
                    .mapToDouble(TaskReportDTO::getHours).sum();
            UserReportDTO preparedReport = new UserReportDTO();
            preparedReport.setDate(date);
            preparedReport.setHours(summedHours);
            preparedReports.add(preparedReport);
        }

        LocalDate today = LocalDate.now();
        double average;
        if (!today.isBefore(fromDate) && !today.isAfter(toDate)) {
            List<UserReportDTO> userReportsUntilToday = preparedReports.stream()
                    .filter(report -> !report.getDate().isAfter(today))
                    .filter(report -> !(FreeDays.isDayFreeOfWork(report.getDate(), true) && report.getHours() == 0))
                    .collect(Collectors.toList());

            average = userReportsUntilToday.stream()
                    .mapToDouble(UserReportDTO::getHours)
                    .average()
                    .orElse(0);
        } else {
            average = preparedReports.stream()
                    .filter(report -> !(FreeDays.isDayFreeOfWork(report.getDate(), true) && report.getHours() == 0))
                    .mapToDouble(UserReportDTO::getHours)
                    .average()
                    .orElse(0);
        }

        double variance = preparedReports.stream().mapToDouble(report -> Math.pow(report.getHours() - average, 2))
                .average()
                .orElse(0);
        double stdDeviation = Math.sqrt(variance);
        for (UserReportDTO report : preparedReports) {
            double hours = report.getHours();
            int status = getStatus(hours, average, stdDeviation, report.getDate()).getValue();
            report.setStatus(status);
        }

        return preparedReports;
    }

    @Override
    public List<TaskReport> findAll() {
        return taskReportRepository.findAll();
    }

    @Override
    public List<TaskReportDTO> findAllByDatesForUser(LocalDate fromDate, LocalDate toDate, Long userId) {

        List<TaskReport> reports = taskReportRepository.findAllByUserIdAndDateBetweenOrderByDateDesc(userId,
                fromDate,
                toDate);

        return taskReportMapper.toDtoList(reports);
    }

    @Override
    public WeeklyTaskReportDTO getEmptyWeeklyReportForTask(Long taskId, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null | toDate == null) {
            throw new BadRequestAlertException("Date cannot be null", "", "datenull");
        }
        if (taskId == null) {
            throw new BadRequestAlertException("Id cannot be null", "", "idnull");
        }
        Task task = taskRepository.findById(taskId).get();
        WeeklyTaskReportDTO weeklyTaskReport = new WeeklyTaskReportDTO();
        weeklyTaskReport.setTaskId(task.getId());
        weeklyTaskReport.setSubprojectCode(task.getSubproject().getCode());
        weeklyTaskReport.setProjectName(task.getSubproject().getProject().getName());
        weeklyTaskReport.setSubprojectName(task.getSubproject().getName());
        weeklyTaskReport.setSubprojectType(task.getSubproject().getSubprojectType().getId());
        weeklyTaskReport.setTaskName(task.getName());
        List<DailyReportDTO> dailyReports = new ArrayList<DailyReportDTO>();
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            DailyReportDTO dailyReportDTO = new DailyReportDTO();
            dailyReportDTO.setDate(date);
            dailyReportDTO.setTaskReports(new ArrayList<TaskReportDTO>());
            dailyReportDTO.setTotalHours(0d);
            dailyReports.add(dailyReportDTO);
        }
        weeklyTaskReport.setDailyReports(dailyReports);
        return weeklyTaskReport;
    }

    @Override
    @Transactional
    public List<WeeklySummaryDTO> getWeeklySummaries(LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to get all WeeklyReports for service");
        if (fromDate == null | toDate == null) {
            throw new BadRequestAlertException("Date cannot be null", "", "datenull");
        }
        List<User> users = userRepository.findAll();

        List<WeeklySummaryDTO> weeklySummaries = new ArrayList<>();

        for (User user : users) {
            WeeklySummaryDTO weeklySummary = new WeeklySummaryDTO();
            List<TaskReportDTO> reports = findAllByDatesForUser(fromDate, toDate, user.getId());

            weeklySummary.setUserId(user.getId());
            weeklySummary.setName(user.getFirstName());
            weeklySummary.setSurname(user.getLastName());

            Double totalHours = reports.stream().filter(report -> !this.isOvertimeOrExitReport(report))
                    .mapToDouble(report -> report.getHours()).sum();
            weeklySummary.setHours(totalHours);

            weeklySummaries.add(weeklySummary);
        }

        return weeklySummaries;
    }

    private boolean isOvertimeOrExitReport(TaskReportDTO taskReport) {
        SubprojectType overtimeSubprojectType = this.subprojectTypeRepository.getOvertimeSubprojectType().orElse(null);
        SubprojectType exitSubprojectType = this.subprojectTypeRepository.getExitsSubprojectType().orElse(null);
        if ((overtimeSubprojectType != null && taskReport.getType() == overtimeSubprojectType.getId())
                || (exitSubprojectType != null && taskReport.getType() == exitSubprojectType.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public List<WeeklyTaskReportDTO> getWeeklyTaskReportsForUser(Long userId, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to get all TotalReports for user: {}", userId);

        if (fromDate == null | toDate == null) {
            throw new BadRequestAlertException("Date cannot be null", "", "datenull");
        } else if (userId == null) {
            throw new BadRequestAlertException("Id cannot be null", "", "idnull");
        }

        User user = userRepository.findById(userId).get();

        List<TaskReportDTO> reports = findAllByDatesForUser(fromDate, toDate, user.getId());

        Map<Long, List<TaskReportDTO>> reportsByTaskId = reports.stream()
                .collect(Collectors.groupingBy(TaskReportDTO::getTaskId));

        List<WeeklyTaskReportDTO> weeklyTaskReports = new ArrayList<>();

        for (Map.Entry<Long, List<TaskReportDTO>> entry : reportsByTaskId.entrySet()) {
            WeeklyTaskReportDTO weeklyTaskReport = new WeeklyTaskReportDTO();
            weeklyTaskReport.setTaskId(entry.getKey());
            weeklyTaskReport.setProjectName(entry.getValue().get(0).getProjectName());
            weeklyTaskReport.setSubprojectName(entry.getValue().get(0).getSubprojectName());
            weeklyTaskReport.setSubprojectCode(entry.getValue().get(0).getSubprojectCode());
            weeklyTaskReport.setSubprojectType(entry.getValue().get(0).getType());
            weeklyTaskReport.setTaskName(entry.getValue().get(0).getTaskName());

            List<DailyReportDTO> dailyReports = new ArrayList<>();
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                final LocalDate finalDate = date;
                DailyReportDTO dailyReport = new DailyReportDTO();
                dailyReport.setDate(date);

                List<TaskReportDTO> reportsForDate = entry.getValue().stream()
                        .filter(report -> report.getDate().equals(finalDate))
                        .collect(Collectors.toList());

                dailyReport.setTotalHours(reportsForDate.stream().mapToDouble(TaskReportDTO::getHours).sum());
                dailyReport.setTaskReports(reportsForDate);

                dailyReports.add(dailyReport);
            }

            weeklyTaskReport.setDailyReports(dailyReports);
            weeklyTaskReports.add(weeklyTaskReport);
        }

        List<Long> defaultTasksIds = taskService.findDefaultTasksIds();

        for (Long id : defaultTasksIds) {
            boolean isPresent = weeklyTaskReports.stream()
                    .anyMatch(weeklyReport -> weeklyReport.getTaskId().equals(id));
            if (!isPresent) {
                WeeklyTaskReportDTO weeklyTaskReport = this.getEmptyWeeklyReportForTask(id, fromDate, toDate);
                weeklyTaskReports.add(weeklyTaskReport);
            }
        }

        List<DefaultTask> defaultTasksForUser = defaultTaskRepository.findAllByUserId(userId);

        for (DefaultTask defaultTask : defaultTasksForUser) {
            boolean isPresent = weeklyTaskReports.stream()
                    .anyMatch(weeklyReport -> weeklyReport.getTaskId().equals(defaultTask.getTask().getId()));
            if (isPresent || defaultTask.getDateFrom().isAfter(toDate)) {
                continue;
            }
            WeeklyTaskReportDTO weeklyTaskReport = this
                    .getEmptyWeeklyReportForTask(defaultTask.getTask().getId(), fromDate, toDate);
            weeklyTaskReports.add(weeklyTaskReport);
        }

        Collections.sort(weeklyTaskReports, (o1, o2) -> {
            int index1 = defaultTasksIds.indexOf(o1.getTaskId());
            int index2 = defaultTasksIds.indexOf(o2.getTaskId());
            if (index1 == -1 && index2 == -1) {
                return o1.getSubprojectCode().compareTo(o2.getSubprojectCode());
            }
            if (index1 == -1) {
                return 1;
            }
            if (index2 == -1) {
                return -1;
            }
            return Integer.compare(index1, index2);
        });

        return weeklyTaskReports;
    }

    @Override
    public void updateWeeklyTaskReportForUser(Long userId, List<WeeklyTaskReportDTO> weeklyTaskReports) {
        log.debug("Request to update user: {} TotalReport", userId);
        if (userId == null) {
            throw new BadRequestAlertException("Id cannot be null", "", "idnull");
        } else if (!userRepository.existsById(userId)) {
            throw new BadRequestAlertException("User not found", "", "idnotfound");
        } else if (weeklyTaskReports == null) {
            throw new BadRequestAlertException("WeeklyTaskReports param cannot be null", "", "incorrectbody");
        }

        for (WeeklyTaskReportDTO weeklyTaskReport : weeklyTaskReports) {
            for (DailyReportDTO dailyReport : weeklyTaskReport.getDailyReports()) {
                List<TaskReportDTO> reportsForDay = taskReportMapper.toDtoList(
                        taskReportRepository.findAllByUserIdAndTaskIdAndDate(userId, weeklyTaskReport.getTaskId(),
                                dailyReport.getDate()));
                double totalHours = reportsForDay.stream()
                        .mapToDouble(TaskReportDTO::getHours)
                        .sum();
                double diff = dailyReport.getTotalHours() - totalHours;
                if (diff == 0) {
                    continue;
                } else if (diff > 0) {
                    if (reportsForDay.size() == 0) {
                        TaskReportDTO newReportDto = new TaskReportDTO();
                        newReportDto.setUserId(userId);
                        newReportDto.setTaskId(weeklyTaskReport.getTaskId());
                        newReportDto.setHours(diff);
                        newReportDto.setDate(dailyReport.getDate());
                        newReportDto.setStatus(false);
                        save(newReportDto);
                    } else {
                        TaskReportDTO reportToUpdate = reportsForDay.get(0);
                        reportToUpdate.setHours(reportToUpdate.getHours() + diff);
                        save(reportToUpdate);
                    }
                } else {
                    for (TaskReportDTO report : reportsForDay) {
                        if (report.getHours() <= diff * -1) {
                            taskReportRepository.deleteById(report.getId());
                        } else {
                            report.setHours(report.getHours() + diff);
                            save(report);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public WeeklyTaskReportDTO addDefaultTaskAndGetEmptyReport(Long userId, Long taskId, LocalDate fromDate,
            LocalDate toDate) {
        if (fromDate == null | toDate == null) {
            throw new BadRequestAlertException("Date cannot be null", "", "datenull");
        }
        if (taskId == null) {
            throw new BadRequestAlertException("TaskId cannot be null", "", "idnull");
        }
        if (userId == null) {
            throw new BadRequestAlertException("UserId cannot be null", "", "idnull");
        }

        Task task = taskRepository.findById(taskId).get();
        User user = userRepository.findById(userId).get();

        List<Long> defaultTasksIds = taskService.findDefaultTasksIds();
        if (defaultTasksIds.contains(task.getId())) {
            throw new BadRequestAlertException("Task is default", "", "defaulttask");
        }

        UserTaskKey id = new UserTaskKey();
        id.setTaskId(task.getId());
        id.setUserId(user.getId());

        DefaultTask foundTask = defaultTaskRepository.findById(id).orElse(null);
        if (foundTask != null) {
            if (foundTask.getDateFrom().isBefore(fromDate) || foundTask.getDateFrom().isEqual(fromDate)) {
                throw new BadRequestAlertException("The task already assigned to user", "", "taskassignedalready");
            }
            foundTask.setDateFrom(fromDate);
            defaultTaskRepository.save(foundTask);
            return getEmptyWeeklyReportForTask(taskId, fromDate, toDate);
        }
        if (taskReportRepository.findAllByUserIdAndTaskIdAndDateBetween(userId, taskId, fromDate, toDate).size() != 0) {
            throw new BadRequestAlertException("The task already assigned.", "", "taskreported");
        }
        DefaultTask defaultTask = new DefaultTask();
        defaultTask.setId(id);
        defaultTask.setUser(user);
        defaultTask.setTask(task);
        defaultTask.setDateFrom(fromDate);
        defaultTaskRepository.save(defaultTask);
        return getEmptyWeeklyReportForTask(taskId, fromDate, toDate);
    }

    @Override
    public void deleteWeeklyReport(Long userId, WeeklyTaskReportDTO weeklyReport) {
        if (weeklyReport.getDailyReports().get(0).getDate() == null
                | weeklyReport.getDailyReports().get(6).getDate() == null) {
            throw new BadRequestAlertException("Date cannot be null", "", "datenull");
        }
        if (userId == null) {
            throw new BadRequestAlertException("User Id cannot be null", "", "idnull");
        }
        if (weeklyReport.getTaskId() == null) {
            throw new BadRequestAlertException("Task Id cannot be null", "", "idnull");
        }
        List<Long> defaultTasksIds = taskService.findDefaultTasksIds();
        if (defaultTasksIds.indexOf(weeklyReport.getTaskId()) != -1) {
            throw new BadRequestAlertException("Cannot delete default task", "", "defaulttaskdeletionrequest");
        }
        if (taskReportRepository.findAllByUserIdAndTaskIdAndDateBetween(userId, weeklyReport.getTaskId(),
                weeklyReport.getDailyReports().get(0).getDate(),
                weeklyReport.getDailyReports().get(6).getDate()).size() != 0) {
            throw new BadRequestAlertException("Cannot delete weekly task with reports", "", "taskreported");
        }
        UserTaskKey id = new UserTaskKey();
        id.setTaskId(taskRepository.findById(weeklyReport.getTaskId()).get().getId());
        id.setUserId(userRepository.findById(userId).get().getId());
        defaultTaskRepository.delete(defaultTaskRepository.findById(id).get());
    }

    @Override
    public TaskReportDTO save(TaskReportDTO taskReportDTO) {
        log.debug("Request to save TaskReport : {}", taskReportDTO);
        TaskReport taskReport = taskReportMapper.toEntity(taskReportDTO);
        TaskReport saveTaskReport = taskReportRepository.save(taskReport);
        return taskReportMapper.toDto(saveTaskReport);
    }

    public byte[] getCSVForUser(LocalDate fromDate, LocalDate toDate) {
        List<UserReportDTO> userReports = this.getMonthlyUserReport(fromDate, toDate);
        LocalDate today = LocalDate.now();
        double average;
        if (!today.isBefore(fromDate) && !today.isAfter(toDate)) {
            List<UserReportDTO> userReportsUntilToday = userReports.stream()
                    .filter(report -> !report.getDate().isAfter(today))
                    .filter(report -> !(FreeDays.isDayFreeOfWork(report.getDate(), true) && report.getHours() == 0))
                    .collect(Collectors.toList());

            average = userReportsUntilToday.stream()
                    .mapToDouble(UserReportDTO::getHours)
                    .average()
                    .orElse(0);
        } else {
            average = userReports.stream()
                    .filter(report -> !(FreeDays.isDayFreeOfWork(report.getDate(), true) && report.getHours() == 0))
                    .mapToDouble(UserReportDTO::getHours)
                    .average()
                    .orElse(0);
        }

        double variance = userReports.stream().mapToDouble(report -> Math.pow(report.getHours() - average, 2)).average()
                .orElse(0);
        double stdDeviation = Math.sqrt(variance);

        StringBuilder csvBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        csvBuilder.append("Data;Godziny;Status\n");

        for (UserReportDTO report : userReports) {
            String formattedDate = report.getDate().format(formatter);
            double hours = report.getHours();
            String status = getStatus(hours, average, stdDeviation, report.getDate()).getDescription();
            csvBuilder.append(formattedDate).append(";").append(hours).append(";").append(status).append("\n");
        }

        return csvBuilder.toString().getBytes();
    }

    private WorkDayStatus getStatus(double hours, double average, double stdDeviation, LocalDate date) {
        boolean isFreeDay = FreeDays.isDayFreeOfWork(date, true);
        LocalDate today = LocalDate.now();
        boolean isFutureWorkdayWithZeroHours = !isFreeDay && date.isAfter(today) && hours == 0;
        double veryLowHoursThreshold = 0.5 * average;
        double slightlyLessHoursThreshold = 0.8 * average;
        double slightOvertimeThreshold = 1.2 * average;
        double lotsOfOvertimeThreshold = 1.5 * average;

        if (isFreeDay) {
            if (hours > 0) {
                return WorkDayStatus.OVERTIME_ON_DAY_OFF;
            } else {
                return WorkDayStatus.NO_WORK_ON_DAY_OFF;
            }
        } else if (isFutureWorkdayWithZeroHours) {
            return WorkDayStatus.AVERAGE_HOURS;
        } else {
            if (hours < veryLowHoursThreshold) {
                return WorkDayStatus.VERY_LOW_HOURS;
            } else if (hours < slightlyLessHoursThreshold) {
                return WorkDayStatus.LOW_HOURS;
            } else if (hours >= slightlyLessHoursThreshold && hours <= slightOvertimeThreshold) {
                return WorkDayStatus.AVERAGE_HOURS;
            } else if (hours > slightOvertimeThreshold && hours <= lotsOfOvertimeThreshold) {
                return WorkDayStatus.HIGH_HOURS;
            } else {
                return WorkDayStatus.VERY_HIGH_HOURS;
            }
        }
    }

    @Override
    public TaskReportDTO addReport(TaskReportDTO dto) {
        if (dto.getTaskId() == null) {
            throw new BadRequestAlertException("No task id provided", "", "noidprovided");
        }
        if (dto.getUserId() == null) {
            User user = SecurityUtil
                    .getCurrentUserLogin()
                    .flatMap(userRepository::findOneByLogin).get();
            Task task = taskRepository.findById(dto.getTaskId()).get();
            dto.setUserId(user.getId());
            dto.setTaskId(task.getId());
            dto.setStatus(false);

            TaskReport newReport = taskReportMapper.toEntity(dto);

            return taskReportMapper.toDto(taskReportRepository.save(newReport));
        } else {
            return null;
        }
    }
}
