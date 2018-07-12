package by.local.test.model;

import by.local.test.dto.Status;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pipeline")
@Builder
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class Pipeline {
    @Id
    @SequenceGenerator(name = "pk_sequence", sequenceName = "pipeline_pipeline_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "pipeline_id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @CreatedDate
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "is_break")
    private Boolean isBreak;
    @OneToMany(mappedBy = "pipeline", fetch = FetchType.LAZY)
    private List<TaskEntity> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pipeline pipeline = (Pipeline) o;

        if (id != pipeline.id) return false;
        if (name != null ? !name.equals(pipeline.name) : pipeline.name != null) return false;
        if (description != null ? !description.equals(pipeline.description) : pipeline.description != null)
            return false;
        if (status != pipeline.status) return false;
        if (startTime != null ? !startTime.equals(pipeline.startTime) : pipeline.startTime != null) return false;
        if (endTime != null ? !endTime.equals(pipeline.endTime) : pipeline.endTime != null) return false;
        return isBreak != null ? isBreak.equals(pipeline.isBreak) : pipeline.isBreak == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (isBreak != null ? isBreak.hashCode() : 0);
        return result;
    }
}
