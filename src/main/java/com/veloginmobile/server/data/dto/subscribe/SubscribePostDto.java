package com.veloginmobile.server.data.dto.subscribe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscribePostDto {
    String name;
    String title;
    String summary;
    String date;
    int comment;
    int like;
    String img;
    String url;
    List<String> tag = new ArrayList<>();

    public static Comparator<SubscribePostDto> compareByDate = new Comparator<SubscribePostDto>() {
        @Override
        public int compare(SubscribePostDto o1, SubscribePostDto o2) {
            try {
                if (o1.getDate().contains("방금 전") || o2.getDate().contains("방금 전")) {
                    if (o1.getDate().contains("방금 전")) return -1;
                    else return 1;
                }

                if (o1.getDate().contains("분 전") || o2.getDate().contains("분 전")) {
                    if (o1.getDate().contains("분 전") && o2.getDate().contains("분 전")) {
                        return Integer.parseInt(o1.getDate().replace("분 전", "")) >= Integer.parseInt(o2.getDate().replace("분 전", "")) ? 1 : -1;
                    } else if (o1.getDate().contains("분 전")) return -1;
                    else return 1;
                }

                if (o1.getDate().contains("시간 전") || o2.getDate().contains("시간 전")) {
                    if (o1.getDate().contains("시간 전") && o2.getDate().contains("시간 전")) {
                        return Integer.parseInt(o1.getDate().replace("시간 전", "").replace("약 ", "")) >= Integer.parseInt(o2.getDate().replace("시간 전", "").replace("약 ", "")) ? 1 : -1;
                    } else if (o1.getDate().contains("시간 전")) return -1;
                    else return 1;
                }

                if (o1.getDate().contains("어제") || o2.getDate().contains("어제")) {
                    if (o1.getDate().contains("어제")) return -1;
                    else return 1;
                }

                if (o1.getDate().contains("일 전") || o2.getDate().contains("일 전")) {
                    if (o1.getDate().contains("일 전") && o2.getDate().contains("일 전")) {
                        return Integer.parseInt(o1.getDate().replace("일 전", "")) >= Integer.parseInt(o2.getDate().replace("일 전", "")) ? 1 : -1;
                    } else if (o1.getDate().contains("일 전")) return -1;
                    else return 1;
                }

                String o1d = o1.getDate();
                String o2d = o2.getDate();
                Date d1 = new Date(Integer.parseInt(o1d.substring(0, 4)), Integer.parseInt(o1d.substring(6, o1d.indexOf("월"))), Integer.parseInt(o1d.substring(o1d.indexOf("월 ") + 2, o1d.indexOf("일"))));
                Date d2 = new Date(Integer.parseInt(o2d.substring(0, 4)), Integer.parseInt(o2d.substring(6, o2d.indexOf("월"))), Integer.parseInt(o2d.substring(o2d.indexOf("월 ") + 2, o2d.indexOf("일"))));

                if (d1.equals(d2))
                    return 0;
                return -d1.compareTo(d2);
            } catch (RuntimeException e){
                System.out.println("RuntimeExeption: compareDate: " + o1 + o2);
            }
            return 0;
        }
    };
}
