package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "RECEIPT")
public class Receipt {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_RECEIPT")
	@SequenceGenerator(name = "SEQ_RECEIPT", sequenceName = "SEQ_RECEIPT", allocationSize = 1)
	private Long id;

	@JoinColumn(name = "REGISTRATION_ID")
	@ForeignKey(name = "FK_RECEIPT_REGISTRATION")
	@Index(name = "IDX_RECEIPT_REGISTRATION")
	@ManyToOne(optional = false)
	private Registration registration;
}
